package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class DeleteProduct {
    private final ProductStorage productStorage;
    private final AssertUserIsNotDiferentFromSeller assertUserIsNotDiferentFromSeller;
    private final AssertUserExists assertUserExists;
    private final AssertProductExists assertProductExists;
    @Transactional
    public void run (String userId, Integer productId) throws UserNotExistsException {
        assertUserExists.run(userId);
        assertProductExists.run(productId);
        String sellerId=productStorage.getSellerByProductId(productId);
        assertUserIsNotDiferentFromSeller.run(userId, sellerId);
        productStorage.deleteProduct(productId);

    }
}
