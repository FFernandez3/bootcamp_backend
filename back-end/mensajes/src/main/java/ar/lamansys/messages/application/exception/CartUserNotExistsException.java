package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.ECartUserNotExistsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartUserNotExistsException extends CustomException{
    private final Integer cartId;
    private final String appUserId;
    public CartUserNotExistsException(Integer cartId, String appUserId,ECartUserNotExistsException code, HttpStatus status){
        super(String.format("A cart with ID %d doesn´t exists for user with id %s", cartId, appUserId),code.toString(), status);
        this.cartId=cartId;
        this.appUserId=appUserId;
    }
}
