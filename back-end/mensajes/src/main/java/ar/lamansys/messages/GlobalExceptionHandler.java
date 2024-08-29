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

/*
    - Para el manejo de los códigos de excepciones pueden definir un enumerado asociado a la excepción que arrojan, así cada excepción tiene
      su propia lista de códigos. Por ejemplo, para la excepción ProductNotExistsException se definiría un enumerado llamado EProductNotExistsException
      (ver la E que antecede al nombre de la excepción como convención de naming) que dentro definiría como valor "PRODUCT_NOT_FOUND" y sería
      ese código lo que se retornaría. Para ello, deberían definir dentro de las excepciones un atributo "code" que sea del tipo del enumerado
      que crearon.La principal razón para utilizar un enumerado es concentrar los códigos de error asociados a un tipo de excepción, de forma que no haya
      cadenas de caracteres literales (como ocurre aquí) y que el código quede mas ordenado.

    - Otra cosa que veo es que no se retornan DTOs al hacer el control de la excepción. Debería retornarse, ya que el BE debería retornar
      siempre a sistemas externos un DTO, y en éste caso se está retornando un mapa.

    - Veo que se hace un control de excepciones agrupados en el método notExistsHandler. Debería haber
      un ExceptionHandler por cada excepción. No es lo mismo un usuario no encontrado que un producto no encontrado.
*/

/*
- Agrego a su solución, considerar definir un método que construya el objeto que desean retornar, que por como
lo hicieron es el mismo para todas las situaciones. Buscar repetir menos código disminuyendo la cantidad de líneas utilizadas.
*/

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


}
