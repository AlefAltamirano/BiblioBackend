package pe.edu.upeu.BiblioBackend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private BigDecimal costoReposicion;
    private Integer stock;
    private Boolean estado;
    private Long generoId;
    private String generoNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}