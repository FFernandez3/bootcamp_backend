package ar.lamansys.messages.application.cartProduct;

import ar.lamansys.messages.application.cart.AssertCartIsNotFinalized;
import ar.lamansys.messages.application.cart.AssertCartUserExist;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.CartUserNotExistsException;
import ar.lamansys.messages.application.exception.ProductNotInCartException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class DeleteProductFromCart {
    private final CartProductStorage cartProductStorage;
    private final CartStorage cartStorage;
    private final AssertCartUserExist assertCartUserExist;
    private final AssertProductInCartExist assertProductInCartExist;
    private final AssertUserExists assertUserExists;
    private final AssertCartIsNotFinalized assertCartIsNotFinalized;

    /*
      - La variable deletedProduct se setea en ambos cursos del if, por lo que podría sacarse fuera junto a la definición de la variable.
      - La constante 1 tendría que estar definida y no usar un literal, aporta legibilidad al código.
      - Por qué devolver todos los datos de un producto que se quita de un carrito?
      - El caso de uso se encarga de eliminar el producto de un carrito, ¿también debería encargarse de eliminar el carrito si ya no almacena productos?
      -    Lo menciono por dos razones:
      -       - El nombre del caso de uso dice "borrar producto de un carrito" en lugar de "borrar producto de un carrito y el carrito si está vacío"... Si bien esta
      -           obervación es debatible, parece no cumplir con el principio de única responsabilidad.
      -       - Otro caso de uso podría no enterarse que el carrito fue eliminado.
    */
    public CartProductBo run (Integer cartId, Integer productId, String appUserId) throws CartUserNotExistsException, ProductNotInCartException, UserNotExistsException {
        //verificar que el usuario exista
        assertUserExists.run(appUserId);
        //verificar que exista el carrito con el usuario
        assertCartUserExist.run(cartId, appUserId);
        //verificar que el carrito no este siendo procesado
        assertCartIsNotFinalized.run(cartId);
        //verificar que este el producto a borrar en el carrito
        assertProductInCartExist.run(cartId,productId);
        //cantidad de productos del carrito
        long productsInCart= cartProductStorage.countByCartId(cartId);

        CartProductBo deletedProduct;

        if(productsInCart==1){
            //borro el producto del carrito
            deletedProduct= cartProductStorage.deleteProductFromCart(cartId, productId);
            //borro el carrito pq no puede tener 0 productos
            cartStorage.deleteCartById(cartId);
        }
        else{
            //borro el producto del carrito
             deletedProduct= cartProductStorage.deleteProductFromCart(cartId, productId);
            //Actualizo el precio total del carrito
            Integer newTotalPrice= cartProductStorage.calculateTotalPrice(cartId);
            cartStorage.updateTotalPrice(cartId,newTotalPrice);
        }

        return deletedProduct;
    }
}
