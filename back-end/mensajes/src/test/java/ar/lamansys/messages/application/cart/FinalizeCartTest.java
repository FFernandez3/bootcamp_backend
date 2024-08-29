package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.CartIsFinalizedException;
import ar.lamansys.messages.application.exception.ProductPriceChangedException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FinalizeCartTest {
    @Mock
    private CartStorage cartStorage;

    @Mock
    private ProductStorage productStorage;

    @Mock
    private CartProductStorage cartProductStorage;

    @Mock // sin cobertura de test
    private AssertCartUserExist assertCartUserExist;

    @Mock
    private AssertStockAvailable assertStockAvailable;

    @Mock
    private AssertCartIsNotFinalized assertCartIsNotFinalized;

    @InjectMocks
    private FinalizeCart finalizeCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_whenCartIsFinalized_shouldUpdateCartAndProducts() throws Exception {
        // Arrange
        Integer cartId = 1;
        String appUserId = "user5";
        ProductShowCartBo product = new ProductShowCartBo(100, "mayonesa", 50, 10, 500);
        List<ProductShowCartBo> products = List.of(product);

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(products.stream());
        when(productStorage.findPriceByProductId(product.getProductId())).thenReturn(product.getUnitPrice());
        when(productStorage.getStock(product.getProductId())).thenReturn(10);

        // Act
        finalizeCart.run(cartId, appUserId);

        // Assert
        verify(cartStorage).updateIsFinalized(cartId);
        verify(productStorage).updateStock(product.getProductId(), 0);
    }

    @Test
    void run_whenStockNotAvailable_shouldThrowStockNotAvailableException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String appUserId = "user23";
        ProductShowCartBo product = new ProductShowCartBo(100, "mayonesa", 15, 1000, 15000);
        List<ProductShowCartBo> products = List.of(product);

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(products.stream());
        when(productStorage.findPriceByProductId(product.getProductId())).thenReturn(product.getUnitPrice());
        when(productStorage.getStock(product.getProductId())).thenReturn(10);

        doThrow(new StockNotAvailableException(product.getProductId(), product.getQuantity(), 990))
                .when(assertStockAvailable).run(product.getProductId(), product.getQuantity());

        // Act & Assert
        assertThrows(StockNotAvailableException.class, () -> finalizeCart.run(cartId, appUserId));
    }

    @Test
    void run_whenProductPriceChanged_shouldThrowProductPriceChangedException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String appUserId = "user1";
        ProductShowCartBo product = new ProductShowCartBo(10, "ketchup", 5, 100,500 );
        List<ProductShowCartBo> products = List.of(product);

        when(cartProductStorage.findAllByCartId(cartId)).thenReturn(products.stream());
        when(productStorage.findPriceByProductId(product.getProductId())).thenReturn(9);

        // Act & Assert
        ProductPriceChangedException exception = assertThrows(ProductPriceChangedException.class,
                () -> finalizeCart.run(cartId, appUserId));
        assertEquals(product.getProductId(), exception.getProductId());
        assertEquals(product.getUnitPrice(), exception.getOldPrice());
        assertEquals(9, exception.getNewPrice());
    }
    @Test
    void run_whenCartIsAlreadyFinalized_shouldThrowCartIsFinalizedException() throws Exception {
        // Arrange
        Integer cartId = 1;
        String appUserId = "user10";

        doThrow(new CartIsFinalizedException(cartId)).when(assertCartIsNotFinalized).run(cartId);

        // Act & Assert
        assertThrows(CartIsFinalizedException.class, () -> finalizeCart.run(cartId, appUserId));
        verifyNoInteractions(cartProductStorage, productStorage, cartStorage);
    }
}


