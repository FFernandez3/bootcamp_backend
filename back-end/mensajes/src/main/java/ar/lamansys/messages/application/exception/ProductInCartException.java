package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductInCartException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductInCartException extends RuntimeException{
    private final Integer cartId;
    private final Integer productId;
    private EProductInCartException code;
    private HttpStatus status;
    public ProductInCartException(Integer cartId, Integer productId, EProductInCartException code, HttpStatus status){
        super(String.format("Product with id %d is not in cart %d",productId,cartId));
        this.cartId=cartId;
        this.productId=productId;
        this.code=code;
        this.status=status;
    }
}
