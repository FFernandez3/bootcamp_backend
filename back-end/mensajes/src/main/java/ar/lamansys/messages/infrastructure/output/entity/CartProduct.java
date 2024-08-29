package ar.lamansys.messages.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_product")
@Entity
public class CartProduct {

    //Debería ser privado el atributo. Por qué inicializarlo vacío?
    @EmbeddedId
    CartProductId id = new CartProductId();

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "quantity_price", nullable = false)
    private Integer quantityPrice;

}
