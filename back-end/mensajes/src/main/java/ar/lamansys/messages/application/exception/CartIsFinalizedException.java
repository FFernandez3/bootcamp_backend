package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.ECartIsFinalizedException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartIsFinalizedException extends CustomException{
    private Integer cartId;
    public CartIsFinalizedException(Integer cartId, ECartIsFinalizedException code, HttpStatus status){
        super(String.format("The cart with id %d is being processed, you cannot modify it",cartId), code.toString(), status);
        this.cartId=cartId;
    }
}
