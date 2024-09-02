package ar.lamansys.messages.infrastructure.input.rest.product;

import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.AddProduct;
import ar.lamansys.messages.application.product.DeleteProduct;
import ar.lamansys.messages.application.product.ListProducts;
import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.DTO.ProductRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.ProductResponseDTO;
import ar.lamansys.messages.infrastructure.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name="Product",  description = "Operaciones relacionadas con los productos")
@RequestMapping("/product")
public class ProductController {
    private final ListProducts listProducts;
    private final ProductMapper productMapper;
    private final AddProduct addProduct;
    private final DeleteProduct deleteProduct;

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
    @PostMapping
    @Operation(summary = "Crea un producto",
            description = "Este endpoint se utiliza para añadir un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado con exito"),
            @ApiResponse(responseCode = "403", description = "El usuario no coincide con el vendedor al que se le quiere agregar el producto"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> addProduct(@RequestHeader String userId, @Valid @RequestBody ProductRequestDTO productRequestDTO) throws UserNotExistsException {
        addProduct.run(userId, productMapper.productRequestDTOToNewProductBo(productRequestDTO));
        return ResponseEntity.status(201).body("Successfully added product.");
    }
    @DeleteMapping("/{productId}")
    @Operation(summary = "Borra un producto",
            description = "Este endpoint se utiliza para borrar un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado con exito"),
            @ApiResponse(responseCode = "403", description = "El usuario no coincide con el vendedor al que se le quiere borrar el producto"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> deleteProduct(@Parameter(description = "ID del usuario", in = ParameterIn.HEADER, required = true)@RequestHeader String userId, @Parameter(description = "ID del producto", required = true) @PathVariable Integer productId) throws UserNotExistsException {
        deleteProduct.run(userId, productId);
        return ResponseEntity.ok().body("Product deleted successfully.");
    }
}
