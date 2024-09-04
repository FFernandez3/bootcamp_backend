package ar.lamansys.messages.application.product;

import ar.lamansys.messages.application.exception.StockNotAvailableException;
import ar.lamansys.messages.application.exception.codeError.EStockNotAvailableException;
import ar.lamansys.messages.application.product.port.ProductStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.lang.Math.abs;

@AllArgsConstructor
@Service
public class AssertStockAvailable {
    private ProductStorage productStorage;

    public void run(Integer productId, Integer quantity) throws StockNotAvailableException {
        Integer stock = productStorage.getStock(productId);
        if ( stock < quantity) {
            Integer missing= abs(stock-quantity);
            throw new StockNotAvailableException(productId,quantity,missing, EStockNotAvailableException.INSUFFICIENT_STOCK, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
