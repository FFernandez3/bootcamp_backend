package ar.lamansys.messages.application.exception.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ValidationErrorDTO {
    private String field;
    private String message;

}
