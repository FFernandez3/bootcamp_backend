package ar.lamansys.messages.application.cart;

import ar.lamansys.messages.application.cart.port.CartStorage;
import ar.lamansys.messages.application.exception.CartIsFinalizedException;
import ar.lamansys.messages.application.exception.codeError.ECartIsFinalizedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssertCartIsNotFinalized {
    private final CartStorage cartStorage;

    public void run (Integer cartId) throws CartIsFinalizedException{
        if(cartStorage.getIsFinalizedById(cartId)){
            throw new CartIsFinalizedException(cartId, ECartIsFinalizedException.CART_IS_FINALIZED, HttpStatus.CONFLICT);
        }
    }
}
