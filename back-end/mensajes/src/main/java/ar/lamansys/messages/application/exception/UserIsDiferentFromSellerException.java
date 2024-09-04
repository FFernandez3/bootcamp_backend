package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EUserIsDiferentFromSellerException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserIsDiferentFromSellerException extends CustomException{
    private String userId;
    private String sellerId;
    public UserIsDiferentFromSellerException(String userId, String sellerId,EUserIsDiferentFromSellerException code, HttpStatus status) {
        super(String.format("User with id %s cannot modify a product for sale of seller with id %s. Modify your own stock.", userId, sellerId), code.toString(), status);
        this.userId = userId;
        this.sellerId=sellerId;
    }
}
