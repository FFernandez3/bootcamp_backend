package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import ar.lamansys.messages.application.exception.codeError.EUserIsDiferentFromSellerException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertUserIsNotDiferentFromSeller {

    public void run(String userId, String sellerId) throws UserIsDiferentFromSellerException {
        if ( ! userId.equalsIgnoreCase(sellerId) ) {
            throw new UserIsDiferentFromSellerException(userId, sellerId, EUserIsDiferentFromSellerException.USER_DIFERENT_FROM_SELLER, HttpStatus.FORBIDDEN);
        }
    }
}
