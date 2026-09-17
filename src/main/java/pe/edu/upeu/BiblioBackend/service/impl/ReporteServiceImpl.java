package pe.edu.upeu.BiblioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import pe.edu.upeu.BiblioBackend.repository.PrestamoRepository;
import pe.edu.upeu.BiblioBackend.service.service.ReporteService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final PrestamoRepository prestamoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoPorGeneroDTO> obtenerPrestamosPorGenero(LocalDateTime desde, LocalDateTime hasta) {
        return prestamoRepository.reportePrestamosPorGenero(desde, hasta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroMasPrestadoDTO> obtenerLibrosMasPrestados(LocalDateTime desde, LocalDateTime hasta) {
        return prestamoRepository.reporteLibrosMasPrestados(desde, hasta);
    }
}