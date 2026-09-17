package pe.edu.upeu.BiblioBackend.dto.reporte;

import java.math.BigDecimal;

public record LibroMasPrestadoDTO(
        Long libroId,
        String titulo,
        String generoNombre,
        Long cantidadPrestada,
        BigDecimal montoValorizado
) {}