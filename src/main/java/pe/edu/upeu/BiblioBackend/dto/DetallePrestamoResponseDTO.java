package pe.edu.upeu.BiblioBackend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePrestamoResponseDTO {

    private Long id;
    private Long libroId;
    private String titulo;
    private Integer cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal subtotal;
}