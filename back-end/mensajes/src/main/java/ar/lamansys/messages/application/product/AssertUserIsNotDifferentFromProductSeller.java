package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import ar.lamansys.messages.application.exception.codeError.EUserIsDiferentFromSellerException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertUserIsNotDifferentFromProductSeller {
    private final ProductStorage productStorage;
    public void run(String userId,Integer productId) throws UserIsDiferentFromSellerException {
        String sellerId=productStorage.getSellerByProductId(productId);
        if ( ! userId.equalsIgnoreCase(sellerId) ) {
            throw new UserIsDiferentFromSellerException(userId, sellerId, EUserIsDiferentFromSellerException.USER_DIFERENT_FROM_SELLER, HttpStatus.FORBIDDEN);
        }
    }
}
