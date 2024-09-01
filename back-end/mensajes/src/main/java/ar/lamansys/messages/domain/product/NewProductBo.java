package ar.lamansys.messages.domain.product;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class NewProductBo {
    String name;
    Integer stock;
    Integer unitPrice;
    String userId;
}
