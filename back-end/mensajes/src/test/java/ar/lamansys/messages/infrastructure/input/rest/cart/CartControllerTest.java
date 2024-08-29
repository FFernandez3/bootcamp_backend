package ar.lamansys.messages.infrastructure.input.rest.cart;

import ar.lamansys.messages.application.cart.CreateCart;
import ar.lamansys.messages.application.cart.FinalizeCart;
import ar.lamansys.messages.application.cart.GetCartState;
import ar.lamansys.messages.domain.cart.CartStoredBo;
import ar.lamansys.messages.domain.cart.CartSummaryBo;
import ar.lamansys.messages.domain.cart.ProductShowCartBo;
import ar.lamansys.messages.infrastructure.DTO.CartRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.CartResponseDTO;
import ar.lamansys.messages.infrastructure.DTO.CartSummaryDTO;
import ar.lamansys.messages.infrastructure.DTO.ProductShowCartDTO;
import ar.lamansys.messages.infrastructure.mapper.CartMapper;
import ar.lamansys.messages.infrastructure.mapper.CartProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest {
    @Mock
    private CreateCart createCart;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private GetCartState getCartState;

    @Mock
    private CartProductMapper cartProductMapper;

    @Mock // sin cobertura de test?
    private FinalizeCart finalizeCart;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void createCart_whenValidRequest_shouldReturnCartResponse() throws Exception {
        // Arrange
        String userId = "user123";
        CartRequestDTO cartDTO = new CartRequestDTO(20, 3);
        CartResponseDTO responseDTO = new CartResponseDTO(2, userId, 2000, false, "user3");

        when(createCart.run(eq(userId), any())).thenReturn(new CartStoredBo(2, userId, 2000, false, "user3"));
        when(cartMapper.cartStoredBoToCartResponseDTO(any())).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/cart")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"quantity\": 20, \"productId\": 3 }"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"id\": 2, \"appUserId\": \"user123\", \"totalPrice\": 2000, \"isFinalized\": false, \"sellerId\": \"user3\" }"));
    }

    @Test
    void getCartState_whenValidRequest_shouldReturnCartSummary() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user12";
        List<ProductShowCartBo> products = List.of(
                new ProductShowCartBo(1, "Product1", 100, 2, 200),
                new ProductShowCartBo(2, "Product2", 150, 1, 150)
        );
        CartSummaryBo cartSummaryBo = new CartSummaryBo(products, 350);
        CartSummaryDTO responseDTO = new CartSummaryDTO(products.stream()
                .map(product -> new ProductShowCartDTO(
                        product.getProductId(),
                        product.getName(),
                        product.getUnitPrice(),
                        product.getQuantity(),
                        product.getQuantityPrice()))
                .collect(Collectors.toList()), cartSummaryBo.getTotalPrice());


        when(getCartState.run(cartId, userId)).thenReturn(cartSummaryBo);
        when(cartProductMapper.cartSummaryBoToCartSummaryDTO(cartSummaryBo)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/cart/{cartId}", cartId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{"
                        + "  \"products\": ["
                        + "    { \"productId\": 1, \"name\": \"Product1\", \"unitPrice\": 100, \"quantity\": 2, \"quantityPrice\": 200 },"
                        + "    { \"productId\": 2, \"name\": \"Product2\", \"unitPrice\": 150, \"quantity\": 1, \"quantityPrice\": 150 }"
                        + "  ],"
                        + "  \"totalPrice\": 350"
                        + "}"));
    }

    @Test
    void finalizeCart_whenValidRequest_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        Integer cartId = 1;
        String userId = "user33";

        // Act & Assert
        mockMvc.perform(put("/cart/{cartId}/checkout", cartId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart successfully closed"));
    }

}
