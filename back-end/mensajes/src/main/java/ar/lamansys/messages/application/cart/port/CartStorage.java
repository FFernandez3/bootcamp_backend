package ar.lamansys.messages.application.cart.port;

import ar.lamansys.messages.domain.cart.CartStoredBo;

import java.util.Optional;

public interface CartStorage {

    /*
    - Detalle. En lugar de is_finalized, definir isFinalized como en la implementación de esta clase :)
    */
    CartStoredBo createCart(String userId, Integer totalPrice, Boolean is_finalized, String sellerId);
    CartStoredBo getCartExists(String userId, String idSeller);
    Optional<CartStoredBo> findByIdAndAppUserId(Integer id, String appUserId);
    void updateTotalPrice(Integer cartId, Integer newTotalPrice);
    void deleteCartById(Integer cartId);
    Integer getTotalPrice(Integer cartId);
    String getSellerById(Integer cartId);
    boolean getIsFinalizedById(Integer cartId);
    void updateIsFinalized(Integer cartId);
}
