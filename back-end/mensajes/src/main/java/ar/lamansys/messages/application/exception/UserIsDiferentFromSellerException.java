package ar.lamansys.messages.application.exception;

import lombok.Getter;

@Getter
public class UserIsDiferentFromSellerException extends RuntimeException{
    private String userId;
    private String sellerId;
    public UserIsDiferentFromSellerException(String userId, String sellerId) {
        super(String.format("The user with id %s cannot add a product for sale to the seller with id %s. Modify your own stock.", userId, sellerId));
        this.userId = userId;
        this.sellerId=sellerId;
    }
}
