package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EUserIsDiferentFromSellerException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserIsDiferentFromSellerException extends RuntimeException{
    private String userId;
    private String sellerId;
    private EUserIsDiferentFromSellerException code;
    private HttpStatus status;
    public UserIsDiferentFromSellerException(String userId, String sellerId,EUserIsDiferentFromSellerException code, HttpStatus status) {
        super(String.format("User with id %s cannot modify a product for sale of seller with id %s. Modify your own stock.", userId, sellerId));
        this.userId = userId;
        this.sellerId=sellerId;
        this.code=code;
        this.status=status;
    }
}
