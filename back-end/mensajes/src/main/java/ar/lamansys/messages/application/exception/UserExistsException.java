package ar.lamansys.messages.application.exception;

import lombok.Getter;

@Getter
public class UserExistsException extends Throwable {
    private String userId;

    public UserExistsException(String userId) {
        super(String.format("User %s exists", userId));
        this.userId = userId;
    }
}
