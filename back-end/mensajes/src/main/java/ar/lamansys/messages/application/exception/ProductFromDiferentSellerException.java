package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductFromDiferentSellerException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductFromDiferentSellerException extends RuntimeException{
    private final Integer cartId;
    private final Integer productId;
    private EProductFromDiferentSellerException code;
    private HttpStatus status;
    public ProductFromDiferentSellerException(Integer cartId, Integer productId, EProductFromDiferentSellerException code, HttpStatus status){
        super(String.format("The seller of the product with id %d is different from the seller of the cart with id %d",productId,cartId));
        this.cartId=cartId;
        this.productId=productId;
        this.code=code;
        this.status=status;
    }
}
