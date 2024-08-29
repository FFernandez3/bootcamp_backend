package ar.lamansys.messages.infrastructure.input.rest.cartProduct;

import ar.lamansys.messages.application.cartProduct.AddProductToCart;
import ar.lamansys.messages.application.cartProduct.DeleteProductFromCart;
import ar.lamansys.messages.application.cartProduct.UpdateQuantity;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.infrastructure.DTO.CartProductRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.CartProductResponseDTO;
import ar.lamansys.messages.infrastructure.DTO.QuantityDTO;
import ar.lamansys.messages.infrastructure.mapper.CartProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
  - No está bueno que dos controllers diferentes compartan la raíz de su path (en éste caso /cart con el controller de carrito).
  - Veo que todos los endpoints siguen el mismo patrón "/{cartId}/product/{productId}". Me parece que quedaría mejor hacer un DTO que tenga
  esos dos atributos y, relacionándolo con el comentario anterior, renombrar la raíz a algo así como "/cart-product".
*/

@RequiredArgsConstructor
@RestController
@Tag(name="CartProduct",  description = "Operaciones relacionadas con los productos en un carrito")
@RequestMapping("/cart")
public class CartProductController {
    private final UpdateQuantity updateQuantity;
    private final AddProductToCart addProductToCart;
    private final DeleteProductFromCart deleteProductFromCart;
    private final CartProductMapper mapper;

    /*
      - Éste controller tiene muchos parámetros y el DTO tiene un solo atributo lo que hace que practicamente no valga la pena definirlo
        así como está. Hay atributos que podrían ir dentro de ese DTO. En caso de agruparlo lo renombraría para que sea mas representativo
        del flujo que se desea satisfacer.
    */
    @PutMapping("/{cartId}/product/{productId}")
    @Operation(summary = "Actualizar cantidad",
            description = "Este endpoint se utiliza para cambiar la cantidad de un producto agregado al carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada con exito"),
            @ApiResponse(responseCode = "400", description = "El producto no se encuentra en el carrito"),
            @ApiResponse(responseCode = "403", description = "No existe un carrito con ese ID para el usuario especificado"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente"),

    })
    public ResponseEntity<String> updateQuantity(@Parameter(description = "ID del usuario", in = ParameterIn.HEADER, required = true )@RequestHeader String userId, @Parameter(description = "ID del carrito", required = true)@PathVariable Integer cartId, @Parameter(description = "ID del producto", required = true) @PathVariable Integer productId, @Parameter(description = "DTO con la nueva cantidad", required = true) @Valid @RequestBody QuantityDTO quantity){
        updateQuantity.run(cartId, userId, productId, quantity.getQuantity());
        return ResponseEntity.ok("The quantity of the product was succesfully changed");
    }

    /*
      - Se pasan datos como parámetros que podrían estar dentro del DTO que reciben sin ningún problema. Hay una "regla" que dice que
        si se envían mas de 4 parámetros a un método se podría crear un objeto que contenga los mismos. Además, me despierta una apregunta
        la estructura de DTO. Por qué colocar dentro el userId y no fuera como en la mayoría de los endpoints?
    */
    @PostMapping("/{cartId}/product/{productId}")
    @Operation(summary = "Agregar un producto al carrito",
            description = "Este endpoint se utiliza para agregar un producto del mismo vendedor al carrito de compras.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El producto se agregó con exito al carrito"),
            @ApiResponse(responseCode = "400", description = "El producto no se encuentra en el carrito"),
            @ApiResponse(responseCode = "403", description = "No existe un carrito con ese ID para el usuario especificado"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente"),
            @ApiResponse(responseCode = "409", description = "Carrito finalizado")

    })
    public ResponseEntity<String> addProductToCart(@Parameter(description = "ID del carrito", required = true)@PathVariable Integer cartId,@Parameter(description = "ID del producto", required = true) @PathVariable Integer productId,@Parameter(description = "DTO del producto a agregar al carrito", required = true) @Valid @RequestBody CartProductRequestDTO cartProduct) throws UserNotExistsException {
        addProductToCart.run(cartId, productId, cartProduct.getUserId(), cartProduct.getQuantity());
        return ResponseEntity.ok("The product was added to the cart");
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    @Operation(summary = "Borrar un producto del carrito",
            description = "Este endpoint se utiliza para sacar un producto del carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El producto se borro del carrito con exito"),
            @ApiResponse(responseCode = "400", description = "El producto no se encuentra en el carrito"),
            @ApiResponse(responseCode = "403", description = "No existe un carrito con ese ID para el usuario especificado"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente"),
            @ApiResponse(responseCode = "409", description = "Carrito finalizado")
    })
    public ResponseEntity<CartProductResponseDTO>deleteProductFromCart(@Parameter(description = "ID del carrito", required = true)@PathVariable Integer cartId,@Parameter(description = "ID del producto", required = true) @PathVariable Integer productId,@Parameter(description = "ID del usuario",in = ParameterIn.HEADER, required = true) @RequestHeader String userId) throws UserNotExistsException {
        CartProductResponseDTO responseDTO=mapper.cartProductBoToCartProductDTO(deleteProductFromCart.run(cartId, productId, userId));
        return ResponseEntity.ok(responseDTO);
    }


}
