package ar.lamansys.messages;

import ar.lamansys.messages.application.exception.*;
import ar.lamansys.messages.application.exception.DTO.ErrorDTO;
import ar.lamansys.messages.application.exception.DTO.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDTO> customExceptionHandler(CustomException ex) {
        ErrorDTO error = new ErrorDTO(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(error, ex.getStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDTO>> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        List<ValidationErrorDTO> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ValidationErrorDTO(fieldName, errorMessage));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({UserNotExistsException.class, UserSessionNotExists.class})
    public ResponseEntity<Map<String, String>> notExistsHandler(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "NOT_FOUND");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> existsHandler(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "BAD_REQUEST");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
