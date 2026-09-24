package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroRequestDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, max = 150, message = "El titulo debe tener entre 3 y 150 caracteres")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 3, max = 120, message = "El autor debe tener entre 3 y 120 caracteres")
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(regexp = "^(978|979)?\\d{9}(\\d|X)$", message = "El ISBN debe tener un formato valido de 10 o 13 digitos")
    private String isbn;

    @NotNull(message = "El costo de reposicion es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo de reposicion debe ser mayor a 0")
    private BigDecimal costoReposicion;

    @NotNull(message = "El stock de ejemplares es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El ID del genero es obligatorio")
    @Positive(message = "El ID del genero debe ser un entero positivo")
    private Long generoId;
}

