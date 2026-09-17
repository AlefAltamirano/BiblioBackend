package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePrestamoRequestDTO {

    @NotNull(message = "El ID del libro es obligatorio")
    @Positive(message = "El ID del libro debe ser valido")
    private Long libroId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe prestar al menos 1 ejemplar")
    private Integer cantidad;
}