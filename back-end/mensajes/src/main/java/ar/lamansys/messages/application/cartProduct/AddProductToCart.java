package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.AssertCartUserExist;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.*;
import ar.lamansys.messages.application.product.AssertProductExists;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.application.user.AssertUserExists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class AddProductToCart {
    private final CartProductStorage cartProductStorage;
    private final ProductStorage productStorage;
    private final CartStorage cartStorage;
    private final AssertUserExists assertUserExists;
    private final AssertProductExists assertProductExists;
    private final AssertCartUserExist assertCartUserExist;
    private final AssertProductNotInCartExist assertProductNotInCartExist;
    private final AssertStockAvailable assertStockAvailable;

    public void run(Integer cartId, Integer productId, String userId, Integer quantity) throws CartUserNotExistsException, ProductNotInCartException, StockNotAvailableException, UserNotExistsException, ProductNotExistsException {
        //chequear que el usuario exista
        assertUserExists.run(userId);
        //chequear que el producto exista
        assertProductExists.run(productId);
        //chequear que el carrito exista y pertenezca al usuario
        assertCartUserExist.run(cartId, userId);
        //chequear que el producto este en el carrito
        assertProductNotInCartExist.run(cartId, productId);
        //verificar que la cantidad no supere el stock disponible
        assertStockAvailable.run(productId, quantity);

        //calculo el quantityPrice
        Integer quantityPrice= quantity * productStorage.getUnitPrice(productId);

        //calculo el nuevo total price
        Integer newTotalPrice= cartProductStorage.calculateTotalPrice(cartId);

        //agrego el producto al carrito
        cartProductStorage.addProductToCart(cartId,productId, quantity, quantityPrice);

        // actualizar el precio total en carrito
        cartStorage.updateTotalPrice(cartId, newTotalPrice);
    }

}
