package ar.lamansys.messages.application.product;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.product.NewProductBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AddProductTest {

    @Mock
    private ProductStorage productStorage;

    @Mock
    private AssertUserExists assertUserExists;

    @InjectMocks
    private AddProduct addProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_userExistsAndIsNotDifferent_savesProduct() throws UserNotExistsException, UserIsDiferentFromSellerException {
        // Arrange
        String userId = "user1";
        NewProductBo newProductBo = new NewProductBo("Product1", 40, 600);

        // Act
        addProduct.run(userId, newProductBo);

        // Assert
        verify(assertUserExists).run(userId);
        verify(productStorage).save(userId, newProductBo);
    }

    @Test
    void run_userDoesNotExist_throwsUserNotExistsException() throws UserNotExistsException {
        // Arrange
        String userId = "user1";
        NewProductBo newProductBo = new NewProductBo("Product1", 40, 60);

        doThrow(new UserNotExistsException(userId)).when(assertUserExists).run(userId);

        // Act and Assert
        UserNotExistsException thrown = assertThrows(UserNotExistsException.class, () -> {
            addProduct.run(userId, newProductBo);
        });

        assertEquals("User "+ userId+ " don't exists", thrown.getMessage());
        verify(assertUserExists).run(userId);
        verifyNoInteractions(productStorage);
    }
}
