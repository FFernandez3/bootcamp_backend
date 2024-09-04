package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.ProductNotInCartException;
import ar.lamansys.messages.application.exception.codeError.EProductNotInCartException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertProductInCartExist {
    private final CartProductStorage cartProductStorage;

    public void run(Integer cartId, Integer productId) throws ProductNotInCartException{
        if(cartProductStorage.findByCartIdAndProductId(cartId, productId) == null){
            throw new ProductNotInCartException(cartId, productId, EProductNotInCartException.PRODUCT_IN_CART_MISMATCH, HttpStatus.NOT_FOUND);
        }

    }
}
