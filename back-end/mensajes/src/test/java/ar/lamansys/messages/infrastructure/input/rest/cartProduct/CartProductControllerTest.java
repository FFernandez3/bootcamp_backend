package ar.lamansys.messages.infrastructure.input.rest.cartProduct;

import ar.lamansys.messages.application.cartProduct.AddProductToCart;
import ar.lamansys.messages.application.cartProduct.DeleteProductFromCart;
import ar.lamansys.messages.application.cartProduct.UpdateQuantity;
import ar.lamansys.messages.domain.cartProduct.CartProductBo;
import ar.lamansys.messages.infrastructure.DTO.CartProductRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.CartProductResponseDTO;
import ar.lamansys.messages.infrastructure.DTO.QuantityDTO;
import ar.lamansys.messages.infrastructure.mapper.CartProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartProductControllerTest {

    @Mock // sin cobertura de test?
    private UpdateQuantity updateQuantity;

    @Mock // sin cobertura de test?
    private AddProductToCart addProductToCart;

    @Mock
    private DeleteProductFromCart deleteProductFromCart;

    @Mock
    private CartProductMapper mapper;

    @InjectMocks
    private CartProductController cartProductController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartProductController).build();
    }

    @Test
    void updateQuantity_whenValidRequest_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        String userId = "user123";
        Integer cartId = 1;
        Integer productId = 10;
        QuantityDTO quantityDTO = new QuantityDTO(5);

        // Act & Assert
        mockMvc.perform(put("/cart/{cartId}/product/{productId}", cartId, productId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"quantity\": 5 }"))
                .andExpect(status().isOk())
                .andExpect(content().string("The quantity of the product was succesfully changed"));
    }

    @Test
    void addProductToCart_whenValidRequest_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 10;
        CartProductRequestDTO cartProductDTO = new CartProductRequestDTO("user123", 3);

        // Act & Assert
        mockMvc.perform(post("/cart/{cartId}/product/{productId}", cartId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userId\": \"user123\", \"quantity\": 3 }"))
                .andExpect(status().isOk())
                .andExpect(content().string("The product was added to the cart"));
    }

    @Test
    void deleteProductFromCart_whenValidRequest_shouldReturnCartProductResponse() throws Exception {
        // Arrange
        Integer cartId = 1;
        Integer productId = 10;
        Integer quantity = 5;
        Integer quantityPrice = 100; // Suponiendo que este es el precio total de la cantidad

        // Creando CartProductBo con los datos correctos
        CartProductBo cartProductBo = new CartProductBo(cartId, productId, quantity, quantityPrice);

        // Creando CartProductResponseDTO con los datos correctos
        CartProductResponseDTO responseDTO = new CartProductResponseDTO(cartId, productId, quantity, quantityPrice);

        // Mockeando las llamadas del servicio
        when(deleteProductFromCart.run(cartId, productId, "user123")).thenReturn(cartProductBo);
        when(mapper.cartProductBoToCartProductDTO(cartProductBo)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(delete("/cart/{cartId}/product/{productId}", cartId, productId)
                        .header("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"cartId\": 1, \"productId\": 10, \"quantity\": 5, \"quantityPrice\": 100 }"));
    }

}
