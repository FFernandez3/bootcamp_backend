package ar.lamansys.messages.infrastructure.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductRequestDTO {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Integer stock;
    @NotNull
    @Positive
    private Integer unitPrice;
}
