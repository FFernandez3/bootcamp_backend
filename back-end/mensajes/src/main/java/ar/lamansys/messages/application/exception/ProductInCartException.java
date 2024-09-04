package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductInCartException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductInCartException extends CustomException{
    private final Integer cartId;
    private final Integer productId;
    public ProductInCartException(Integer cartId, Integer productId, EProductInCartException code, HttpStatus status){
        super(String.format("Product with id %d is not in cart %d",productId,cartId), code.toString(), status);
        this.cartId=cartId;
        this.productId=productId;

    }
}
