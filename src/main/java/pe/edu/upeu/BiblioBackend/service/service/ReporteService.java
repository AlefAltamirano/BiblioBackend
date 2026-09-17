package pe.edu.upeu.BiblioBackend.service.service;

import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {
    List<PrestamoPorGeneroDTO> obtenerPrestamosPorGenero(LocalDateTime desde, LocalDateTime hasta);
    List<LibroMasPrestadoDTO> obtenerLibrosMasPrestados(LocalDateTime desde, LocalDateTime hasta);
}