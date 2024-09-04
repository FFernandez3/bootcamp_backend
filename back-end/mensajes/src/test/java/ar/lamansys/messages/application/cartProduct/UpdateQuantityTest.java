package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.AssertCartIsNotFinalized;
import ar.lamansys.messages.application.cart.AssertCartUserExist;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.CartUserNotExistsException;
import ar.lamansys.messages.application.exception.codeError.ECartUserNotExistsException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UpdateQuantityTest {
    @Mock
    private CartProductStorage cartProductStorage;

    @Mock
    private ProductStorage productStorage;

    @Mock
    private AssertCartUserExist assertCartUserExist;

    @Mock
    private AssertProductInCartExist assertProductInCartExist;

    @Mock
    private AssertStockAvailable assertStockAvailable;

    @Mock
    private CartStorage cartStorage;

    @Mock
    private AssertCartIsNotFinalized assertCartIsNotFinalized;

    @InjectMocks
    private UpdateQuantity updateQuantity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_whenAllAssertionsPass_shouldUpdateQuantityAndTotalPrice() throws Exception {
        // Arrange
        Integer cartId = 6;
        String appUserId = "user3";
        Integer productId = 121;
        Integer newQuantity = 3;
        Integer unitPrice = 500;
        Integer quantityPrice = newQuantity * unitPrice;
        Integer newTotalPrice = 2500;

        when(productStorage.getUnitPrice(productId)).thenReturn(unitPrice);
        when(cartProductStorage.calculateTotalPrice(cartId)).thenReturn(newTotalPrice);

        // Act
        updateQuantity.run(cartId, appUserId, productId, newQuantity);

        // Assert
        verify(assertCartUserExist).run(cartId, appUserId);
        verify(assertCartIsNotFinalized).run(cartId);
        verify(assertProductInCartExist).run(cartId, productId);
        verify(assertStockAvailable).run(productId, newQuantity);
        verify(cartProductStorage).updateQuantity(cartId, productId, newQuantity, quantityPrice);
        verify(cartStorage).updateTotalPrice(cartId, newTotalPrice);
    }

    @Test
    void run_whenCartUserNotExists_shouldThrowCartUserNotExistsException() {
        // Arrange
        Integer cartId = 96;
        String appUserId = "user34";
        Integer productId = 10;
        Integer newQuantity = 3;

        doThrow(new CartUserNotExistsException(cartId, appUserId, ECartUserNotExistsException.CART_MISMATCH, HttpStatus.FORBIDDEN)).when(assertCartUserExist).run(cartId, appUserId);

        // Act & Assert
        assertThrows(CartUserNotExistsException.class, () -> updateQuantity.run(cartId, appUserId, productId, newQuantity));

        verify(assertCartUserExist).run(cartId, appUserId);
        verifyNoMoreInteractions(assertCartIsNotFinalized, assertProductInCartExist, assertStockAvailable, cartProductStorage, cartStorage);
    }

}

