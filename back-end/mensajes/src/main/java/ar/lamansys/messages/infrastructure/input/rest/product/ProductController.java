package ar.lamansys.messages.infrastructure.input.rest.product;

import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.ListProducts;
import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.DTO.ProductResponseDTO;
import ar.lamansys.messages.infrastructure.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

// Para el path del endpoint quitaría el plural de products y lo dejaría product (como la política de naming de las tablas de la base de datos).

@RestController
@AllArgsConstructor
@Tag(name="Product",  description = "Operaciones relacionadas con los productos")
@RequestMapping("/products")
public class ProductController {
    private final ListProducts listProducts;
    private final ProductMapper productMapper;

    @GetMapping("/{userId}")
    @Operation(summary = "Obtiene productos de un vendedor",
            description = "Este endpoint se utiliza para obtener los productos vendidos por el usuario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos del vendedor obtenidas con exito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
    public ResponseEntity<List<ProductResponseDTO>> productsFromSeller(@Parameter(description = "ID del usuario vendedor", required = true)@PathVariable String userId) throws UserNotExistsException {
        List<ProductStoredBo> products = listProducts.run(userId);
        List<ProductResponseDTO> response = productMapper.boListToProductResponseListDTO(products);
        return ResponseEntity.status(200).body(response);
    }

}
