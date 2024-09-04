package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductNotInCartException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotInCartException extends RuntimeException{
    private final Integer cartId;
    private final Integer productId;
    private EProductNotInCartException code;
    private HttpStatus status;
    public ProductNotInCartException(Integer cartId, Integer productId, EProductNotInCartException code, HttpStatus status){
        super(String.format("Product with id %d is not in cart %d",productId,cartId));
        this.cartId=cartId;
        this.productId=productId;
        this.code=code;
        this.status=status;
    }
}
