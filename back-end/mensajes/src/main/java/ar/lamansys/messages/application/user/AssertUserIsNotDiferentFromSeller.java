package ar.lamansys.messages.application.user;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertUserIsNotDiferentFromSeller {

    public void run(String userId, String sellerId) throws UserIsDiferentFromSellerException {
        if ( ! userId.equalsIgnoreCase(sellerId) ) {
            throw new UserIsDiferentFromSellerException(userId, sellerId);
        }
    }
}
