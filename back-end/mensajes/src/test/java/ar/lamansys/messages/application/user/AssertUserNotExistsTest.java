package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserExistsException;
import ar.lamansys.messages.application.user.AssertUserNotExists;
import ar.lamansys.messages.application.user.port.UserStorage;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AssertUserNotExistsTest {

    UserStorage userStorage = Mockito.mock(UserStorage.class);
    AssertUserNotExists assertUserNotExists = new AssertUserNotExists(userStorage);


    @Test
    void userExistence_ok() {
        when(userStorage.exists("userId")).thenReturn(false);
        assertDoesNotThrow(() -> assertUserNotExists.run("userId"));
    }

    @Test
    void userExistence_notOk() {
        when(userStorage.exists("userId")).thenReturn(true);
        assertThrows(UserExistsException.class, () -> assertUserNotExists.run("userId"));
    }

}