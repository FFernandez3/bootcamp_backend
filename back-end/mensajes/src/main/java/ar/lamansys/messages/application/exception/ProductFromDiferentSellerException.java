package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductFromDiferentSellerException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductFromDiferentSellerException extends CustomException{
    private final Integer cartId;
    private final Integer productId;
    public ProductFromDiferentSellerException(Integer cartId, Integer productId, EProductFromDiferentSellerException code, HttpStatus status){
        super(String.format("The seller of the product with id %d is different from the seller of the cart with id %d",productId,cartId), code.toString(), status);
        this.cartId=cartId;
        this.productId=productId;
    }
}
