package ar.lamansys.messages.application.exception;

import lombok.Getter;

@Getter
public class UserIsDiferentFromSellerException extends RuntimeException{
    private String userId;
    private String sellerId;
    public UserIsDiferentFromSellerException(String userId, String sellerId) {
        super(String.format("User with id %s cannot modify a product for sale of seller with id %s. Modify your own stock.", userId, sellerId));
        this.userId = userId;
        this.sellerId=sellerId;
    }
}
