package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.application.user.port.UserStorage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AssertUserExistsTest {

    private UserStorage userStorage =
            Mockito.mock(UserStorage.class);

    private AssertUserExists assertUserExists = new AssertUserExists(userStorage);

    @Test
    void userExistence_ok() {
        Mockito.when(userStorage.exists("userId")).thenReturn(true);
        assertDoesNotThrow(() -> assertUserExists.run("userId"));
    }

    @Test
    void userNotExistence_throwsException() {
        //Arrange
        Mockito.when(userStorage.exists("userId")).thenReturn(false);
        //Act and Assert
        assertThrows(UserNotExistsException.class, () -> assertUserExists.run("userId"));
    }

}