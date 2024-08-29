package ar.lamansys.messages.infrastructure.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CartSummaryDTO {
    @Schema(description = "Lista de productos en el carrito",
            example = "[{\"productId\": 1, \"name\": \"Mayonesa\", \"unitPrice\": 50, \"quantity\": 2, \"quantityPrice\": 100}]")
    private List<ProductShowCartDTO> products;

    //Falta el private al atributo
    @Schema(description = "Precio total de todos los productos en el carrito", example = "100")
    Integer totalPrice;
}
