package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EStockNotAvailableException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StockNotAvailableException extends RuntimeException {
    private Integer productId;
    private Integer quantity;
    private EStockNotAvailableException code;
    private HttpStatus status;

    public StockNotAvailableException(Integer productId, Integer quantity, Integer missing, EStockNotAvailableException code, HttpStatus status) {
        super(String.format("There is not enough stock to supply the quantity of %s for the product with id %s. %d quantities are missing", quantity, productId, missing));
        this.productId = productId;
        this.quantity = quantity;
        this.code=code;
        this.status=status;
    }
}
