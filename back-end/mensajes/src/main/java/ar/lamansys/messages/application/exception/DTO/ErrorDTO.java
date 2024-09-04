package ar.lamansys.messages.application.exception.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorDTO {
    private String message;
    private String code;
}
