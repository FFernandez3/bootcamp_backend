package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.cartProduct.port.CartProductStorage;
import ar.lamansys.messages.application.exception.CartIsFinalizedException;
import ar.lamansys.messages.application.exception.CartUserNotExistsException;
import ar.lamansys.messages.application.exception.ProductPriceChangedException;
import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.exception.codeError.EProductPriceChangedException;
import ar.lamansys.messages.application.product.AssertStockAvailable;
import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Transactional
@Service
public class FinalizeCart {
    private final CartStorage cartStorage;
    private final ProductStorage productStorage;
    private final CartProductStorage cartProductStorage;
    private final AssertCartUserExist assertCartUserExist;
    private final AssertStockAvailable assertStockAvailable;
    private final AssertCartIsNotFinalized assertCartIsNotFinalized;

    public void run(Integer cartId, String appUserId) throws CartUserNotExistsException, StockNotAvailableException, CartIsFinalizedException {
        assertCartUserExist.run(cartId, appUserId);
        assertCartIsNotFinalized.run(cartId);

        // Obtén los productos del carrito
        List<ProductShowCartBo> products = cartProductStorage.findAllByCartId(cartId).collect(Collectors.toList());
        for (ProductShowCartBo product : products) {
            // Verifica que hay suficiente stock
            assertStockAvailable.run(product.getProductId(), product.getQuantity());

            // Verifica que el precio no ha cambiado
            Integer currentUnitPrice = productStorage.findPriceByProductId(product.getProductId());
            if (!product.getUnitPrice().equals(currentUnitPrice)) {
                throw new ProductPriceChangedException(product.getProductId(), product.getUnitPrice(), currentUnitPrice, EProductPriceChangedException.OUTDATED_CART_PRICE, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        //actualizar is_finalized
        cartStorage.updateIsFinalized(cartId);

        //actualizar stock en Product de todos los productos del carrito
        for (ProductShowCartBo product : products) {
            Integer newStock = productStorage.getStock(product.getProductId()) - product.getQuantity();
            productStorage.updateStock(product.getProductId(), newStock);
        }


    }
}
