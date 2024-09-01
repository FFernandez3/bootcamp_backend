package ar.lamansys.messages.infrastructure.mapper;
import ar.lamansys.messages.domain.product.NewProductBo;
import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.DTO.ProductRequestDTO;
import ar.lamansys.messages.infrastructure.DTO.ProductResponseDTO;
import ar.lamansys.messages.infrastructure.DTO.CartResponseDTO;
import ar.lamansys.messages.infrastructure.output.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductMapper() {
    }

    public ProductResponseDTO productStoredBoToProductResponseDTO(ProductStoredBo productStoredBo) {
        return new ProductResponseDTO(productStoredBo.getName(), productStoredBo.getStock(), productStoredBo.getUnitPrice(), productStoredBo.getUserId());
    }

    public List<ProductResponseDTO> boListToProductResponseListDTO(List<ProductStoredBo> boList) {
        return boList.stream()
                .map(this::productStoredBoToProductResponseDTO)
                .collect(Collectors.toList());
    }
    public NewProductBo productRequestDTOToNewProductBo(ProductRequestDTO requestDTO){
        return new NewProductBo(
                requestDTO.getName(),
                requestDTO.getStock(),
                requestDTO.getUnitPrice(),
                requestDTO.getUserId());
    }


}
