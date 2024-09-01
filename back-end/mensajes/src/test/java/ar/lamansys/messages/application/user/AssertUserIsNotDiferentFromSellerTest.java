package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

public class AssertUserIsNotDiferentFromSellerTest {
    private final AssertUserIsNotDiferentFromSeller assertUserIsNotDiferentFromSeller = new AssertUserIsNotDiferentFromSeller();

    @Test
    void run_userIdIsSameAsSellerId_doesNotThrowException() throws UserIsDiferentFromSellerException {
        // Arrange
        String userId = "user1";
        String sellerId = "user1";

        // Act and Assert
        assertDoesNotThrow(() -> {
            assertUserIsNotDiferentFromSeller.run(userId, sellerId);
        });
    }

    @Test
    void run_userIdIsDifferentFromSellerId_throwsUserIsDiferentFromSellerException() {
        // Arrange
        String userId = "user1";
        String sellerId = "differentUser";

        // Act and Assert
        UserIsDiferentFromSellerException thrown = assertThrows(UserIsDiferentFromSellerException.class, () -> {
            assertUserIsNotDiferentFromSeller.run(userId, sellerId);
        });

        // Assert
        assertEquals(userId, thrown.getUserId());
        assertEquals(sellerId, thrown.getSellerId());
        assertEquals("The user with id "+userId+" cannot add a product for sale to the seller with id "+sellerId+". Modify your own stock.", thrown.getMessage());
    }
}
