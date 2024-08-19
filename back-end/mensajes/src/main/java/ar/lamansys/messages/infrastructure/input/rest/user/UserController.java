package ar.lamansys.messages.infrastructure.input.rest.user;

import ar.lamansys.messages.application.ClearData;
import ar.lamansys.messages.application.user.AddUser;
import ar.lamansys.messages.application.user.ClearUsers;
import ar.lamansys.messages.application.user.SetUserSession;
import ar.lamansys.messages.application.exception.UserExistsException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.exception.UserSessionNotExists;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final AddUser addUser;
    private final ClearUsers clearUsers;
    private final ClearData.GetUserSession getUserSession;
    private final SetUserSession setUserSession;

    @GetMapping("/session")
    public String getUserSession() throws UserSessionNotExists {
        return getUserSession.run();
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody String userId) throws UserExistsException {
        addUser.run(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Object> clearUsers() {
        clearUsers.run();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/session")
    public void setUserSession(@RequestBody String userId) throws UserNotExistsException {
        setUserSession.run(userId);
    }
}
