package ar.lamansys.messages.application.cart;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.exception.OpenCartException;
import ar.lamansys.messages.application.exception.codeError.EOpenCartException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertOpenCartBetweenSellerAndBuyerNotExists {
    private ProductStorage productStorage;
    private CartStorage cartStorage;

    public void run(String userId, Integer productId) throws OpenCartException {
        //chequear si yo compradora no tengo un carrito abierto con el vendedor del producto agregado al carrito
        String idSeller= productStorage.getSellerByProductId(productId);
        if (cartStorage.getCartExists(userId,idSeller) != null) {
            throw new OpenCartException(idSeller, EOpenCartException.OPEN_CART_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }
    }

}
