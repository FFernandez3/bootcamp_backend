package ar.lamansys.messages.infrastructure.output.impl;

import ar.lamansys.messages.application.product.port.ProductStorage;
import ar.lamansys.messages.domain.product.NewProductBo;
import ar.lamansys.messages.domain.product.ProductStoredBo;
import ar.lamansys.messages.infrastructure.output.entity.Product;
import ar.lamansys.messages.infrastructure.output.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ProductStorageImpl implements ProductStorage {
    private final ProductRepository repository;

    @Override
    public void save(String userId, NewProductBo newProductBo) { repository.save(new Product(userId, newProductBo)); }

    @Override
    public Stream<ProductStoredBo> findAllByUserId(String sellerId) {
        return repository.findAllByUserId(sellerId);
    }

    @Override
    public boolean exists(Integer productId) {
        return repository.existsById(productId);
    }

    @Override
    public Integer getStock(Integer productId) {
        return repository.getStock(productId);
    }

    @Override
    public String getSellerByProductId(Integer productId) {
        return repository.getSellerByProductId(productId);
    }

    @Override
    public Integer getUnitPrice(Integer productId) {
        return repository.getUnitPrice(productId);
    }

    @Override
    public Integer findPriceByProductId(Integer productId) {
        return repository.findPriceByProductId(productId);
    }

    @Override
    public void updateStock(Integer productId, Integer newStock) {
        repository.updateStock( productId, newStock);
    }

    @Override
    public void deleteProduct(Integer productId) {
        repository.deleteById(productId);
    }

}
