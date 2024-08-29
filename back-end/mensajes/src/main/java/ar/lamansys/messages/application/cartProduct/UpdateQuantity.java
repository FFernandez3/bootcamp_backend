package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.AssertCartIsNotFinalized;
import ar.lamansys.messages.application.cart.AssertCartUserExist;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.CartUserNotExistsException;
import ar.lamansys.messages.application.exception.ProductNotInCartException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class UpdateQuantity {
    private final CartProductStorage cartProductStorage;
    private final ProductStorage productStorage;
    private final AssertCartUserExist assertCartUserExist;
    private final AssertProductInCartExist assertProductInCartExist;
    private final AssertStockAvailable assertStockAvailable;
    private final CartStorage cartStorage;
    private final AssertCartIsNotFinalized assertCartIsNotFinalized;

    /*
    - Lo anoto acá pero este comentario se refiere a la ALTA/BAJA/MODIFICACION del producto. Si prestan atención a los tres
      casos de uso, lo único que varían es la operación de actualización sobre el producto en el carrito, propagando
      las validaciones previas, y el cálculo posterior del nuevo precio del carrito. Al principio esto funciona bien,
      por eso es que esto no es grave, de hecho esta es la manera sencilla para comenzar el desarrollo de las features porque
      al principio no conocemos hasta donde van a evolucionar... Sin embargo pensemos que puede que haga falta otro caso de uso
      más, que se ocupe de estas reglas del negocio tanto previas como posteriores al cambio (que las centralicen,
      quitándoles así la responsabilidad delegada a cada caso de uso de hacerlas cumplir).
      Puede que para tres casos de usos internos en el proyecto alcance y sobre, pero en el futuro puede no ser deseable y mantenible.
     */
    public void run(Integer cartId, String appUserId, Integer productId, Integer newQuantity) throws CartUserNotExistsException, ProductNotInCartException, StockNotAvailableException {

        //chequear que el carrito exista y pertenezca al usuario
        assertCartUserExist.run(cartId, appUserId);

        //chequear que el carrito no este siendo procesado
        assertCartIsNotFinalized.run(cartId);

        //chequear que el producto este en el carrito
        assertProductInCartExist.run(cartId, productId);

        //verificar que la cantidad no supere el stock disponible
        assertStockAvailable.run(productId, newQuantity);

        //actualizar quantity_price cantidad * unit_price (producto)
        Integer quantityPrice=newQuantity*productStorage.getUnitPrice(productId);

        //actualizo la cantidad y quantity_price en CartProduct
        cartProductStorage.updateQuantity(cartId,productId, newQuantity, quantityPrice);

       //calculo el nuevo total price
        Integer newTotalPrice= cartProductStorage.calculateTotalPrice(cartId);

        // actualizar el precio total en carrito
        cartStorage.updateTotalPrice(cartId, newTotalPrice);


    }

}
