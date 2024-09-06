package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.product.NewProductBo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddProduct {
    private final ProductStorage productStorage;
    private final AssertUserExists assertUserExists;

    public void run (String userId, NewProductBo newProductBo) throws UserNotExistsException, UserIsDiferentFromSellerException {
        assertUserExists.run(userId);
        productStorage.save(userId, newProductBo);
    }
}
