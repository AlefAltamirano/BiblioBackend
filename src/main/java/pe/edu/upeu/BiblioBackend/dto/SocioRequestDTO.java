package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocioRequestDTO {

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe contener exactamente 8 digitos numericos")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar una direccion de email valida")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    @Pattern(regexp = "^\\d{9}$", message = "El telefono debe contener 9 digitos numericos")
    private String telefono;

    @Size(max = 250, message = "La direccion no debe superar los 250 caracteres")
    private String direccion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}