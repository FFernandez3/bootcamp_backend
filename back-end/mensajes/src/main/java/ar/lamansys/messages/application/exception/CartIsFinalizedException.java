package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.ECartIsFinalizedException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartIsFinalizedException extends RuntimeException{
    private Integer cartId;
    private ECartIsFinalizedException code;
    private HttpStatus status;

    public CartIsFinalizedException(Integer cartId, ECartIsFinalizedException code, HttpStatus status){
        super(String.format("The cart with id %d is being processed, you cannot modify it",cartId));
        this.cartId=cartId;
        this.code=code;
        this.status=status;
    }
}
