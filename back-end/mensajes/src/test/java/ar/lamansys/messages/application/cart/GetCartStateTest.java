package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.OpenCartException;
import ar.lamansys.messages.application.exception.ProductNotExistsException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.exception.codeError.EOpenCartException;
import ar.lamansys.messages.application.exception.codeError.EStockNotAvailableException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.cart.CartSummaryBo;
import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetCartStateTest {

    @Mock
    private CartStorage cartStorage;

    @Mock
    private AssertUserExists assertUserExists;

    @Mock
    private AssertStockAvailable assertStockAvailable;

    @Mock
    private CartProductStorage cartProductStorage;

    @Mock
    private AssertCartUserExist assertCartUserExist;

    @InjectMocks
    private GetCartState getCartState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_whenValidInput_shouldReturnCartSummary() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user123";
        Integer totalPrice = 5000;

        ProductShowCartBo product1 = new ProductShowCartBo(1, "Product1", 1000, 2, 2000);
        ProductShowCartBo product2 = new ProductShowCartBo(2, "Product2", 1500, 2, 3000);
        List<ProductShowCartBo> products = List.of(product1, product2);

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(products.stream());
        when(cartStorage.getTotalPrice(cartId)).thenReturn(totalPrice);

        // Act
        CartSummaryBo result = getCartState.run(cartId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(products.size(), result.getProducts().size());
        assertEquals(totalPrice, result.getTotalPrice());
        verify(assertUserExists).run(userId);
        verify(assertCartUserExist).run(cartId, userId);
        verify(assertStockAvailable).run(product1.getProductId(), product1.getQuantity());
        verify(assertStockAvailable).run(product2.getProductId(), product2.getQuantity());
    }

    @Test
    void run_whenUserNotExists_shouldThrowUserNotExistsException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user123";

        doThrow(new UserNotExistsException(userId)).when(assertUserExists).run(userId);

        // Act & Assert
        assertThrows(UserNotExistsException.class, () -> getCartState.run(cartId, userId));
    }

    @Test
    void run_whenCartUserNotExists_shouldThrowOpenCartException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user123";

        doNothing().when(assertUserExists).run(userId);
        doThrow(new OpenCartException("user12", EOpenCartException.OPEN_CART_ALREADY_EXIST, HttpStatus.BAD_REQUEST)).when(assertCartUserExist).run(cartId, userId);

        // Act & Assert
        assertThrows(OpenCartException.class, () -> getCartState.run(cartId, userId));
    }

    @Test
    void run_whenStockNotAvailable_shouldThrowStockNotAvailableException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user123";
        ProductShowCartBo product = new ProductShowCartBo(1, "Product1", 1000, 10, 10000);

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(Stream.of(product));
        doThrow(new StockNotAvailableException(product.getProductId(), product.getQuantity(), 5, EStockNotAvailableException.INSUFFICIENT_STOCK, HttpStatus.UNPROCESSABLE_ENTITY)).when(assertStockAvailable).run(product.getProductId(), product.getQuantity());

        // Act & Assert
        assertThrows(StockNotAvailableException.class, () -> getCartState.run(cartId, userId));
    }

    @Test
    void run_whenNoProductsInCart_shouldReturnEmptyProductList() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user123";
        Integer totalPrice = 0;

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(Stream.empty());
        when(cartStorage.getTotalPrice(cartId)).thenReturn(totalPrice);

        // Act
        CartSummaryBo result = getCartState.run(cartId, userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
        assertEquals(totalPrice, result.getTotalPrice());
        verify(assertUserExists).run(userId);
        verify(assertCartUserExist).run(cartId, userId);
        verify(cartProductStorage).findAllByCartId(cartId);
        verify(cartStorage).getTotalPrice(cartId);
    }
}
