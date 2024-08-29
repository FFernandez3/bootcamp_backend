package ar.lamansys.messages.infrastructure.output.repository;

import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import ar.lamansys.messages.infrastructure.output.entity.CartProduct;
import ar.lamansys.messages.infrastructure.output.entity.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {

    @Query("SELECT NEW ar.lamansys.messages.domain.cart.ProductShowCartBo(p.id, p.name, p.unitPrice, cp.quantity, cp.quantityPrice) " +
            "FROM CartProduct cp " +
            "JOIN Product p ON cp.id.productId = p.id " +
            "WHERE cp.id.cartId = :cartId")
    Stream<ProductShowCartBo> findAllByCartId(@Param("cartId") Integer cartId);


    //Éste método actualiza el precio además de la cantidad. Lo renobraría para ser mas específico.
    @Modifying
    @Query("UPDATE CartProduct cp SET cp.quantity = :quantity, cp.quantityPrice = :quantityPrice WHERE cp.id.cartId = :cartId AND cp.id.productId = :productId")
    Integer updateQuantity(@Param("quantity") Integer quantity, @Param("cartId") Integer cartId, @Param("productId") Integer productId, @Param("quantityPrice") Integer quantityPrice);

    @Query("SELECT NEW ar.lamansys.messages.domain.cartProduct.CartProductBo(cp.id.cartId, cp.id.productId,cp.quantity, cp.quantityPrice) FROM CartProduct cp WHERE cp.id.cartId = :cartId AND cp.id.productId = :productId")
    CartProductBo findByCartIdAndProductId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);

    @Query("SELECT sum(cp.quantityPrice) FROM CartProduct cp WHERE cp.id.cartId = :cartId")
    Integer calculateTotalPrice(@Param("cartId") Integer cartId);

    // Por qué generar un insert utilizando una query nativa y no creando una instancia de la entidad y usar el save provisto por JPA?
    @Modifying
    @Query(value = "INSERT INTO cart_product (cart_id, product_id, quantity, quantity_price) VALUES (:cartId, :productId, :quantity, :quantityPrice)", nativeQuery = true)
    Integer addProductToCart(@Param("cartId") Integer cartId, @Param("productId") Integer productId, @Param("quantity") Integer quantity, @Param("quantityPrice") Integer quantityPrice);

    @Modifying
    @Query("DELETE FROM CartProduct cp WHERE cp.id.cartId = :cartId AND cp.id.productId = :productId")
    void deleteProductFromCart(@Param("cartId") Integer cartId,@Param("productId") Integer productId);

    //Una forma un poco mas eficiente de hacer ésto sería hacer un COUNT(1), ya que los valores de CartProduct no me importan en absoluto.
    @Query("SELECT COUNT(cp) FROM CartProduct cp WHERE cp.id.cartId = :cartId")
    long countByCartId(@Param("cartId") Integer cartId);
}
