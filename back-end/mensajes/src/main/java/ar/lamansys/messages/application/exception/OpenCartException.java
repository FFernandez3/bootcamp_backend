package ar.lamansys.messages.application.exception;

import ar.lamansys.messages.application.exception.codeError.EOpenCartException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OpenCartException extends CustomException {
    private String idSeller;
    public OpenCartException(String idSeller, EOpenCartException code, HttpStatus status) {
        super(String.format("A cart already exists between you and the seller %s", idSeller), code.toString(), status);
        this.idSeller = idSeller;
    }
}
