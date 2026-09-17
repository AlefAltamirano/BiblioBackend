package pe.edu.upeu.BiblioBackend.dto.reporte;

import java.math.BigDecimal;

public record PrestamoPorGeneroDTO(
        Long generoId,
        String generoNombre,
        Long cantidadPrestada,
        BigDecimal montoValorizado
) {}