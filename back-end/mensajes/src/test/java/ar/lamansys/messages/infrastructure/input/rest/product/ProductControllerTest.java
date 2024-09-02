package ar.lamansys.messages.infrastructure.input.rest.product;

import ar.lamansys.messages.application.exception.UserIsDiferentFromSellerException;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.product.AddProduct;
import ar.lamansys.messages.application.product.DeleteProduct;
import ar.lamansys.messages.application.product.ListProducts;
import ar.lamansys.messages.domain.product.NewProductBo;
import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.DTO.ProductRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.ProductResponseDTO;
import ar.lamansys.messages.infrastructure.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ListProducts listProducts;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private DeleteProduct deleteProduct;

    @MockBean
    private AddProduct addProduct;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void productsFromSeller_ok() throws Exception {
        // Arrange
        String userId = "user1";
        List<ProductStoredBo> expectedProducts = List.of(
                new ProductStoredBo("Product1", 40, 600, userId)
        );
        List<ProductResponseDTO> expectedResponse = List.of(
                new ProductResponseDTO("Product1", 40, 600, userId)
        );

        when(listProducts.run(userId)).thenReturn(expectedProducts);
        when(productMapper.boListToProductResponseListDTO(expectedProducts)).thenReturn(expectedResponse);

        // Act y Assert
        mockMvc.perform(get("/product/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponse.size()))
                .andExpect(jsonPath("$[0].name").value("Product1"));
    }

    @Test
    void productsFromSeller_throwException() throws Exception {
        // Arrange
        String userId = "invalidUserId";
        when(listProducts.run(userId)).thenThrow(new UserNotExistsException(userId));

        // Act y Assert
        mockMvc.perform(get("/product/{userId}", userId))
                .andExpect(status().isNotFound());
    }
    @Test
    void addProduct_ok() throws Exception {
        // Arrange
        String userId = "user1";
        ProductRequestDTO productRequestDTO = new ProductRequestDTO("Product1", 40, 600, userId);
        NewProductBo newProductBo = new NewProductBo("Product1", 40, 600, userId);

        when(productMapper.productRequestDTOToNewProductBo(productRequestDTO)).thenReturn(newProductBo);

        // Act y Assert
        mockMvc.perform(post("/product")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Product1\", \"stock\": 40, \"unitPrice\": 600, \"userId\": \"" + userId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successfully added product."));

    }
    @Test
    void deleteProduct_ok() throws Exception {
        // Given
        String userId = "validUserId";
        Integer productId = 1;

        // When
        doNothing().when(deleteProduct).run(userId, productId);

        // Then
        mockMvc.perform(delete("/product/{productId}", productId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully."));
    }

    @Test
    void deleteProduct_userNotExists_throwUserNotExistsException() throws Exception {
        // Given
        String userId = "nonExistentUserId";
        Integer productId = 1;

        // When
        doThrow(new UserNotExistsException(userId)).when(deleteProduct).run(userId, productId);

        // Then
        mockMvc.perform(delete("/product/{productId}", productId)
                        .header("userId", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserNotExistsException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("User "+userId+" don't exists"));
    }

    @Test
    void deleteProduct_throwUserIsDiferentFromSellerException() throws Exception {
        // Given
        String userId = "userIdNotAuthorized";
        Integer productId = 1;
        String sellerId="user1";

        // When
        doThrow(new UserIsDiferentFromSellerException(userId, sellerId))
                .when(deleteProduct).run(userId, productId);

        // Then
        mockMvc.perform(delete("/product/{productId}", productId)
                        .header("userId", userId))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIsDiferentFromSellerException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("User with id "+userId+" cannot modify a product for sale of seller with id "+sellerId +". Modify your own stock."));
    }

}

