package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.ProductNotExistsException;
import ar.lamansys.messages.application.exception.codeError.EProductNotExistsException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service

public class AssertProductExists {
    private ProductStorage productStorage;

    public void run(Integer productId) throws ProductNotExistsException {
        if ( ! productStorage.exists(productId) ) {
            throw new ProductNotExistsException(productId, EProductNotExistsException.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
