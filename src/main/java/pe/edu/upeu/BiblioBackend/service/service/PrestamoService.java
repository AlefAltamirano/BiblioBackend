package pe.edu.upeu.BiblioBackend.service.service;

import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO request);
    PrestamoResponseDTO obtenerPorId(Long id);
    PrestamoResponseDTO registrarDevolucion(Long id);
    PrestamoResponseDTO anularPrestamo(Long id);
    List<PrestamoResponseDTO> buscarConFiltros(Long socioId, EstadoPrestamo estado, LocalDateTime desde, LocalDateTime hasta, String orden);
}