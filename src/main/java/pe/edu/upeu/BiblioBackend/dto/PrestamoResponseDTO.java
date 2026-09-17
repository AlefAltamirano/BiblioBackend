package pe.edu.upeu.BiblioBackend.dto;

import lombok.*;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoResponseDTO {

    private Long id;
    private LocalDateTime fecha;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estado;
    private Long socioId;
    private String socioNombre;
    private BigDecimal totalValorizado;
    private List<DetallePrestamoResponseDTO> detalles;
}