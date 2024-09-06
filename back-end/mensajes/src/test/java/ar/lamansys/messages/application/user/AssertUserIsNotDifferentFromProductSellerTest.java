package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import ar.lamansys.messages.application.exception.codeError.EUserIsDiferentFromSellerException;
import ar.lamansys.messages.application.product.AssertUserIsNotDifferentFromProductSeller;
import ar.lamansys.messages.application.product.port.ProductStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class AssertUserIsNotDifferentFromProductSellerTest {
    @Mock
    private ProductStorage productStorage;

    @InjectMocks
    private AssertUserIsNotDifferentFromProductSeller assertUserIsNotDifferentFromProductSeller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_userIdMatchesSellerId_noExceptionThrown() {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        String sellerId = "user123";

        when(productStorage.getSellerByProductId(productId)).thenReturn(sellerId);

        // Act & Assert
        assertUserIsNotDifferentFromProductSeller.run(userId, productId);

        verify(productStorage, times(1)).getSellerByProductId(productId);
    }

    @Test
    void run_userIdDoesNotMatchSellerId_throwsUserIsDiferentFromSellerException() {
        // Arrange
        String userId = "user123";
        Integer productId = 1;
        String sellerId = "user456";

        when(productStorage.getSellerByProductId(productId)).thenReturn(sellerId);

        // Act & Assert
        UserIsDiferentFromSellerException exception = assertThrows(
                UserIsDiferentFromSellerException.class,
                () -> assertUserIsNotDifferentFromProductSeller.run(userId, productId)
        );

        assert exception.getUserId().equals(userId);
        assert exception.getSellerId().equals(sellerId);
        assert exception.getStatus() == HttpStatus.FORBIDDEN;
        assert Objects.equals(exception.getCode(), EUserIsDiferentFromSellerException.USER_DIFERENT_FROM_SELLER.toString());

        verify(productStorage, times(1)).getSellerByProductId(productId);
    }


}
