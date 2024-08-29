package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.AssertCartIsNotFinalized;
import ar.lamansys.messages.application.cart.AssertCartUserExist;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteProductFromCartTest {
    @Mock
    private CartProductStorage cartProductStorage;

    @Mock
    private CartStorage cartStorage;

    @Mock // sin cobertura de test
    private AssertCartUserExist assertCartUserExist;

    @Mock // sin cobertura de test
    private AssertProductInCartExist assertProductInCartExist;

    @Mock // sin cobertura de test
    private AssertUserExists assertUserExists;

    @Mock
    private AssertCartIsNotFinalized assertCartIsNotFinalized;

    @InjectMocks
    private DeleteProductFromCart deleteProductFromCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void run_whenProductIsOnlyProductInCart_shouldDeleteProductAndCart() throws Exception {
        // Arrange
        Integer cartId = 87;
        Integer productId = 20;
        String appUserId = "user7";
        CartProductBo cartProductBo = new CartProductBo(productId, cartId, 5, 500);

        when(cartProductStorage.countByCartId(cartId)).thenReturn(1L);
        when(cartProductStorage.deleteProductFromCart(cartId, productId)).thenReturn(cartProductBo);
        doNothing().when(cartStorage).deleteCartById(cartId);

        // Act
        CartProductBo result = deleteProductFromCart.run(cartId, productId, appUserId);

        // Assert
        assertNotNull(result);
        assertEquals(cartProductBo, result);
        verify(cartProductStorage).deleteProductFromCart(cartId, productId);
        verify(cartStorage).deleteCartById(cartId);
    }

    @Test
    void run_whenProductIsInCartButNotOnlyProduct_shouldDeleteProductAndUpdateTotalPrice() throws Exception {
        // Arrange
        Integer cartId = 6;
        Integer productId = 350;
        String appUserId = "user35";
        CartProductBo cartProductBo = new CartProductBo(productId, cartId, 5, 1000);
        when(cartProductStorage.countByCartId(cartId)).thenReturn(2L);
        when(cartProductStorage.deleteProductFromCart(cartId, productId)).thenReturn(cartProductBo);
        when(cartProductStorage.calculateTotalPrice(cartId)).thenReturn(5000);

        // Act
        CartProductBo result = deleteProductFromCart.run(cartId, productId, appUserId);

        // Assert
        assertNotNull(result);
        assertEquals(cartProductBo, result);
        verify(cartProductStorage).deleteProductFromCart(cartId, productId);
        verify(cartProductStorage).calculateTotalPrice(cartId);
        verify(cartStorage).updateTotalPrice(cartId, 5000);
    }


}
