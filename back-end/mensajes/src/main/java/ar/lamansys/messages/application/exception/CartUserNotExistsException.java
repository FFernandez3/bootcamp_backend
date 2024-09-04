package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.ECartUserNotExistsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartUserNotExistsException extends RuntimeException{
    private final Integer cartId;
    private final String appUserId;
    private ECartUserNotExistsException code;
    private HttpStatus status;
    public CartUserNotExistsException(Integer cartId, String appUserId,ECartUserNotExistsException code, HttpStatus status){
        super(String.format("A cart with ID %d doesn´t exists for user with id %s", cartId, appUserId));
        this.cartId=cartId;
        this.appUserId=appUserId;
        this.code=ECartUserNotExistsException.CART_MISMATCH;
        this.status=status;
    }
}
