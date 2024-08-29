package ar.lamansys.messages.domain.cartProduct;
/*
- Siguiendo conveciones de código de java, en la definición del package se acostumbra no utilizar mayúsculas (esto es un detalle ínfimo).
*/

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
/*
- Recuerden que la anotación @Value reemplaza el alcance de las variables definidas en la clase asociada por private-package.
Entonces para todos los casos definir "private" deja impreciso o confuso el código fuente.
*/
public class CartProductBo {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private Integer quantityPrice;
}
