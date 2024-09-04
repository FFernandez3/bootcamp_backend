package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductPriceChangedException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductPriceChangedException extends RuntimeException {
    private final Integer productId;
    private final Integer oldPrice;
    private final Integer newPrice;
    private EProductPriceChangedException code;
    private HttpStatus status;
    public ProductPriceChangedException(Integer productId, Integer oldPrice, Integer newPrice, EProductPriceChangedException code, HttpStatus status) {
        super(String.format("The price of product with id %d has changed from %d to %d", productId, oldPrice, newPrice));
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.code=code;
        this.status=status;
    }
}