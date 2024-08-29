package ar.lamansys.messages.infrastructure.output.repository;

import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.output.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT NEW ar.lamansys.messages.domain.product.ProductStoredBo(p.name, p.stock, p.unitPrice, p.userId) " +
            "FROM Product p " +
            "WHERE p.userId = :userId")
    Stream<ProductStoredBo> findAllByUserId(@Param("userId") String userId);

    @Query("SELECT NEW ar.lamansys.messages.domain.product.ProductStoredBo(p.name, p.stock, p.unitPrice, p.userId) " +
            "FROM Product p " +
            "WHERE p.id = :productId")
    Optional<ProductStoredBo> findProductById(@Param("productId") Integer productId);

    @Query("SELECT p.stock " +
            "FROM Product p " +
            "WHERE p.id = :productId")
    Integer getStock(@Param("productId") Integer productId);

    @Query("SELECT p.userId " +
            "FROM Product p " +
            "WHERE p.id = :productId")
    String getSellerByProductId(@Param("productId") Integer productId);

    /*
    - getUnitPrice y findPriceByProductId ¿hacen lo mismo, no?
     */
    @Query("SELECT p.unitPrice " +
            "FROM Product p " +
            "WHERE p.id = :productId")
    Integer getUnitPrice(@Param("productId") Integer productId);

    @Query("SELECT p.unitPrice " +
            "FROM Product p" +
            " WHERE p.id=:productId")
    Integer findPriceByProductId(@Param("productId")Integer productId);

    /*
      Cuando usamos operaciones  de borrado o actualización solemos utilizar la etiqueta @Transactional para indicar
      que la operación no es solo lectura (ya que por defecto el atributo readOnly se encuentra en false).
    */
    @Modifying
    @Query("UPDATE Product p" +
            " SET p.stock=:newStock" +
            " WHERE p.id=:productId")
    void updateStock(@Param("productId")Integer productId, @Param("newStock")Integer newStock);
}

