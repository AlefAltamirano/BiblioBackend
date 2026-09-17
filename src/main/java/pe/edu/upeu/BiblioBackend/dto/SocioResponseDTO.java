package pe.edu.upeu.BiblioBackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocioResponseDTO {

    private Long id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}