package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserExistsException;
import ar.lamansys.messages.application.user.port.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddUser {
    private final UserStorage userStorage;
    private final AssertUserNotExists assertUserNotExists;

    public void run(String userId) throws UserExistsException {
        assertUserNotExists.run(userId);
        userStorage.save(userId);
    }
}
