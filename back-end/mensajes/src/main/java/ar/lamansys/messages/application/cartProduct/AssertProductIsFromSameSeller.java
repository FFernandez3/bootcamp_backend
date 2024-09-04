package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.exception.ProductFromDiferentSellerException;
import ar.lamansys.messages.application.exception.codeError.EProductFromDiferentSellerException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AssertProductIsFromSameSeller {
    private CartStorage cartStorage;
    private ProductStorage productStorage;

    public void run (Integer cartId, Integer productId) throws ProductFromDiferentSellerException{
        if(! cartStorage.getSellerById(cartId).equalsIgnoreCase(productStorage.getSellerByProductId(productId))){
            throw new ProductFromDiferentSellerException(cartId, productId, EProductFromDiferentSellerException.PRODUCT_FROM_DIFFERENT_SELLER, HttpStatus.CONFLICT);
        }
    }
}
