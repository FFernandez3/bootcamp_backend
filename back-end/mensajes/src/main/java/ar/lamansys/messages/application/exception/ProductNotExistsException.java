package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EProductNotExistsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotExistsException extends RuntimeException {
    private Integer productId;
    private EProductNotExistsException code;
    private HttpStatus status;

    public ProductNotExistsException(Integer productId, EProductNotExistsException code, HttpStatus status) {
        super(String.format("Product %s doesn't exist", productId));
        this.productId = productId;
        this.code=code;
        this.status=status;
    }
}
