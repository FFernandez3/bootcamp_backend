package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.ProductInCartException;
import ar.lamansys.messages.application.exception.codeError.EProductInCartException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertProductNotInCartExist {
    private final CartProductStorage cartProductStorage;

    public void run(Integer cartId, Integer productId) throws ProductInCartException{
        if(cartProductStorage.findByCartIdAndProductId(cartId, productId) != null){
            throw new ProductInCartException(cartId, productId, EProductInCartException.PRODUCT_IN_CART_EXISTS, HttpStatus.BAD_REQUEST);
        }

    }
}
