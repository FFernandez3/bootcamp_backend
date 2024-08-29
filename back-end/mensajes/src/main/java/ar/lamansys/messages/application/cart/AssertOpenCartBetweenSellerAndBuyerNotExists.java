package ar.lamansys.messages.application.cart;
import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.exception.OpenCartException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertOpenCartBetweenSellerAndBuyerNotExists {
    /*
    Agregar el modificador final, obliga al constructor a inicializar sus variables. Al no estar implementado por nosotros,
    debemos asegurarnos que el framework lo haga (delegamos al @AllArgsConstructor que haga la magia).
    Esto no falla, funciona como es esperable, pero el modificador nos indica (a nosotros los desarrolladores) que su inicialización sea obligatoria.
     */
    private ProductStorage productStorage;
    private CartStorage cartStorage;

    public void run(String userId, Integer productId) throws OpenCartException {
        //chequear si yo compradora no tengo un carrito abierto con el vendedor del producto agregado al carrito
        String idSeller= productStorage.getSellerByProductId(productId);
        if (cartStorage.getCartExists(userId,idSeller) != null) {
            throw new OpenCartException(idSeller);
        }
    }

}
