package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteProduct {
    private final ProductStorage productStorage;
    private final AssertUserIsNotDifferentFromProductSeller assertUserIsNotDifferentFromProductSeller;
    private final AssertUserExists assertUserExists;
    private final AssertProductExists assertProductExists;

    public void run (String userId, Integer productId) throws UserNotExistsException {
        assertUserExists.run(userId);
        assertProductExists.run(productId);
        assertUserIsNotDifferentFromProductSeller.run(userId, productId);
        productStorage.deleteProduct(productId);
    }
}
