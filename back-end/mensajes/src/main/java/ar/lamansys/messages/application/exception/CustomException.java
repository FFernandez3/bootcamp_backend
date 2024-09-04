package ar.lamansys.messages.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException{
    private final String code;
    private final HttpStatus status;
    public CustomException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
