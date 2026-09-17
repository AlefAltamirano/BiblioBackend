package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoRequestDTO {

    @NotNull(message = "El socio es obligatorio")
    @Positive(message = "El ID del socio debe ser valido")
    private Long socioId;

    @NotEmpty(message = "El prestamo debe contener al menos un detalle")
    @Valid
    private List<DetallePrestamoRequestDTO> detalles;
}