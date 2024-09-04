package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EOpenCartException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OpenCartException extends RuntimeException {
    private String idSeller;
    private EOpenCartException code;
    private HttpStatus status;
    public OpenCartException(String idSeller, EOpenCartException code, HttpStatus status) {
        super(String.format("A cart already exists between you and the seller %s", idSeller));
        this.idSeller = idSeller;
        this.code=code;
        this.status=status;
    }
}
