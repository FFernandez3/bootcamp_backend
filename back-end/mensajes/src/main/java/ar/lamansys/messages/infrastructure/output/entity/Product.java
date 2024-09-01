package ar.lamansys.messages.infrastructure.output.entity;

import ar.lamansys.messages.domain.product.NewProductBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Entity
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_product", nullable = false)
    private String name;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "app_user_id", nullable = false)
    private String userId;

    public Product(Integer id) {
        this.id = id;
    }
    public Product (NewProductBo bo){
        this.name=bo.getName();
        this.stock=bo.getStock();
        this.unitPrice=bo.getUnitPrice();
        this.userId=bo.getUserId();
    }
}
