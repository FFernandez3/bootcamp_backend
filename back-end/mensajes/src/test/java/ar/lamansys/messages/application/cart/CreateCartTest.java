package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.OpenCartException;
import ar.lamansys.messages.application.exception.ProductNotExistsException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.exception.codeError.EOpenCartException;
import ar.lamansys.messages.application.exception.codeError.EProductNotExistsException;
import ar.lamansys.messages.application.exception.codeError.EStockNotAvailableException;
import ar.lamansys.messages.application.product.AssertProductExists;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.cart.CartStoredBo;
import ar.lamansys.messages.domain.cart.NewCartBo;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateCartTest {

    @Mock
    private CartStorage cartStorage;

    @Mock
    private AssertUserExists assertUserExists;

    @Mock
    private AssertProductExists assertProductExists;

    @Mock
    private AssertStockAvailable assertStockAvailable;

    @Mock
    private AssertOpenCartBetweenSellerAndBuyerNotExists assertOpenCartBetweenSellerAndBuyerNotExists;

    @Mock
    private ProductStorage productStorage;

    @Mock
    private CartProductStorage cartProductStorage;

    @InjectMocks
    private CreateCart createCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_whenValidInput_shouldCreateCartAndReturnCartStoredBo() throws Exception {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        Integer quantity = 2;
        Integer unitPrice = 1000;
        Integer totalPrice = quantity * unitPrice;
        String sellerId = "seller123";
        CartStoredBo cartStoredBo = new CartStoredBo(1, userId, totalPrice, false, sellerId);
        NewCartBo cartBo = new NewCartBo(productId, quantity);

        doNothing().when(assertUserExists).run(userId);
        doNothing().when(assertProductExists).run(productId);
        doNothing().when(assertStockAvailable).run(productId, quantity);
        doNothing().when(assertOpenCartBetweenSellerAndBuyerNotExists).run(userId, productId);

        when(productStorage.getSellerByProductId(productId)).thenReturn(sellerId);
        when(productStorage.getUnitPrice(productId)).thenReturn(unitPrice);
        when(cartStorage.createCart(userId, totalPrice, false, sellerId)).thenReturn(cartStoredBo);

        // Act
        CartStoredBo result = createCart.run(userId, cartBo);

        // Assert
        assertNotNull(result);
        assertEquals(cartStoredBo.getId(), result.getId());
        verify(cartStorage).createCart(userId, totalPrice, false, sellerId);
        verify(cartProductStorage).createCartProduct(new CartProductBo(cartStoredBo.getId(), productId, quantity, totalPrice));
    }

    @Test
    void run_whenUserNotExists_shouldThrowUserNotExistsException() throws Exception {
        // Arrange
        String userId = "user123";
        NewCartBo cartBo = new NewCartBo(1, 2);
        doThrow(new UserNotExistsException(userId)).when(assertUserExists).run(userId);

        // Act & Assert
        assertThrows(UserNotExistsException.class, () -> createCart.run(userId, cartBo));
    }

    @Test
    void run_whenProductNotExists_shouldThrowProductNotExistsException() throws Exception {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        NewCartBo cartBo = new NewCartBo(productId, 2);
        doNothing().when(assertUserExists).run(userId);
        doThrow(new ProductNotExistsException(productId, EProductNotExistsException.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND)).when(assertProductExists).run(productId);

        // Act & Assert
        assertThrows(ProductNotExistsException.class, () -> createCart.run(userId, cartBo));
    }

    @Test
    void run_whenStockNotAvailable_shouldThrowStockNotAvailableException() throws Exception {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        Integer quantity = 10;
        NewCartBo cartBo = new NewCartBo(productId, quantity);
        doNothing().when(assertUserExists).run(userId);
        doNothing().when(assertProductExists).run(productId);
        doThrow(new StockNotAvailableException(productId, quantity, 5, EStockNotAvailableException.INSUFFICIENT_STOCK, HttpStatus.UNPROCESSABLE_ENTITY)).when(assertStockAvailable).run(productId, quantity);

        // Act & Assert
        assertThrows(StockNotAvailableException.class, () -> createCart.run(userId, cartBo));
    }

    @Test
    void run_whenOpenCartExists_shouldThrowOpenCartException() throws Exception {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        NewCartBo cartBo = new NewCartBo(productId, 2);
        doNothing().when(assertUserExists).run(userId);
        doNothing().when(assertProductExists).run(productId);
        doNothing().when(assertStockAvailable).run(productId, cartBo.getQuantity());
        doThrow(new OpenCartException("seller123", EOpenCartException.OPEN_CART_ALREADY_EXIST, HttpStatus.BAD_REQUEST)).when(assertOpenCartBetweenSellerAndBuyerNotExists).run(userId, productId);

        // Act & Assert
        assertThrows(OpenCartException.class, () -> createCart.run(userId, cartBo));
    }
}
