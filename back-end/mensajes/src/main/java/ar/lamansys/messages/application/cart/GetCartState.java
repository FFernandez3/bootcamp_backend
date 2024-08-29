package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.OpenCartException;
import ar.lamansys.messages.application.exception.ProductNotExistsException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.cart.CartStoredBo;
import ar.lamansys.messages.domain.cart.CartSummaryBo;
import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Quedaron imports sin utilizar.

@AllArgsConstructor
/*
- ¿Es realmente necesario definir el caso de uso como @Transactional?
 */
@Transactional
@Service
public class GetCartState {
    private final CartStorage cartStorage;
    private final AssertUserExists assertUserExists;
    private final AssertStockAvailable assertStockAvailable;
    private final CartProductStorage cartProductStorage;
    private final AssertCartUserExist assertCartUserExist;

    /*
      Aquí se buscan por un lado los productos del carrito y por otro, si es que las validaciones son superadas exitosamente, el precio
      del mismo. Haría un único método que retorne un BO que tenga por datos el Stream de productos y su precio. Por qué? Porque se minimiza
      el acceso a la DB y además es preferible tener todos los datos que sean necesarios para completar el flujo una sola vez.
    */
    public CartSummaryBo run(Integer cartId, String userId) throws UserNotExistsException, ProductNotExistsException, StockNotAvailableException, OpenCartException {
        assertUserExists.run(userId);
        assertCartUserExist.run(cartId, userId);
        //chequea stock
        Stream<ProductShowCartBo> productsStream = cartProductStorage.findAllByCartId(cartId);
        List<ProductShowCartBo> products = productsStream.peek(product -> assertStockAvailable.run(product.getProductId(), product.getQuantity()))
                .collect(Collectors.toList());

        Integer totalPrice = cartStorage.getTotalPrice(cartId);

        CartSummaryBo cartSummary = new CartSummaryBo(products, totalPrice);

        return cartSummary;
    }
}
