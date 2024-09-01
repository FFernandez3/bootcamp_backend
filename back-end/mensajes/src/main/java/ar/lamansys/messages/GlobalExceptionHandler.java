package ar.lamansys.messages;

import ar.lamansys.messages.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotExistsException.class, UserSessionNotExists.class, ProductNotExistsException.class})
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
    @ExceptionHandler(CartUserNotExistsException.class)
    public ResponseEntity<Map<String, String>> cartUserNotExistHandler(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "CART_MISMATCH");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OpenCartException.class)
    public ResponseEntity<Map<String, String>> openCartHandler(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "OPEN_CART_ALREADY_EXIST");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(StockNotAvailableException.class)
    public ResponseEntity<Map<String, String>> stockNotAvailableHandler(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "INSUFFICIENT_STOCK");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError) error).getField();
            String errorMessage=error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductNotInCartException.class)
    public ResponseEntity<Map<String, String>> productNotInCartHandler(ProductNotInCartException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "PRODUCT_IN_CART_MISMATCH");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductInCartException.class)
    public ResponseEntity<Map<String, String>> productInCartHandler(ProductInCartException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "PRODUCT_IN_CART_EXISTS");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductFromDiferentSellerException.class)
    public ResponseEntity<Map<String, String>> productFromDiferentSellerHandler(ProductFromDiferentSellerException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "PRODUCT_FROM_DIFFERENT_SELLER");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CartIsFinalizedException.class)
    public ResponseEntity<Map<String, String>> cartIsFinalizedHandler(CartIsFinalizedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "CART_IS_FINALIZED");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProductPriceChangedException.class)
    public ResponseEntity<Map<String, String>> productPriceChangedHandler(ProductPriceChangedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "OUTDATED_CART_PRICE");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(UserIsDiferentFromSellerException.class)
    public ResponseEntity<Map<String, String>> userDiferentFromSellerHandler(UserIsDiferentFromSellerException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "USER_DIFERENT_FROM_SELLER");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


}
