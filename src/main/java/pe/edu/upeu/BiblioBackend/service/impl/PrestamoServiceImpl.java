package pe.edu.upeu.BiblioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.DetallePrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.DetallePrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.DetallePrestamo;
import pe.edu.upeu.BiblioBackend.entity.Libro;
import pe.edu.upeu.BiblioBackend.entity.Prestamo;
import pe.edu.upeu.BiblioBackend.entity.Socio;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.repository.PrestamoRepository;
import pe.edu.upeu.BiblioBackend.repository.SocioRepository;
import pe.edu.upeu.BiblioBackend.service.service.PrestamoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final SocioRepository socioRepository;
    private final LibroRepository libroRepository;

    @Override
    @Transactional
    public PrestamoResponseDTO registrarPrestamo(PrestamoRequestDTO request) {
        log.info("Registrando prestamo para el socio ID: {}", request.getSocioId());

        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con ID: " + request.getSocioId()));

        if (!socio.getEstado()) {
            throw new ReglaNegocioException("El socio se encuentra inactivo y no puede recibir prestamos.");
        }

        Prestamo prestamo = Prestamo.builder()
                .socio(socio)
                .fecha(LocalDateTime.now())
                .fechaDevolucionPrevista(LocalDate.now().plusDays(7)) // Regla: 7 dias de prestamo
                .estado(EstadoPrestamo.REGISTRADO)
                .totalValorizado(BigDecimal.ZERO)
                .build();

        BigDecimal totalAcumulado = BigDecimal.ZERO;

        for (DetallePrestamoRequestDTO detalleDTO : request.getDetalles()) {
            Libro libro = libroRepository.findById(detalleDTO.getLibroId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con ID: " + detalleDTO.getLibroId()));

            if (!libro.getEstado()) {
                throw new ReglaNegocioException("El libro '" + libro.getTitulo() + "' no esta activo para prestamos.");
            }

            if (libro.getStock() < detalleDTO.getCantidad()) {
                throw new ReglaNegocioException("Stock insuficiente para el libro '" + libro.getTitulo() +
                        "'. Disponible: " + libro.getStock() + ", Requerido: " + detalleDTO.getCantidad());
            }

            // Actualizacion de Stock
            libro.setStock(libro.getStock() - detalleDTO.getCantidad());
            libroRepository.save(libro);

            BigDecimal subtotal = libro.getCostoReposicion().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
            totalAcumulado = totalAcumulado.add(subtotal);

            DetallePrestamo detalle = DetallePrestamo.builder()
                    .libro(libro)
                    .cantidad(detalleDTO.getCantidad())
                    .costoUnitario(libro.getCostoReposicion())
                    .subtotal(subtotal)
                    .build();

            prestamo.agregarDetalle(detalle);
        }

        prestamo.setTotalValorizado(totalAcumulado);
        Prestamo guardado = prestamoRepository.save(prestamo);

        return mapToResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO obtenerPorId(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con ID: " + id));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO registrarDevolucion(Long id) {
        log.info("Registrando devolucion de ejemplares para el prestamo ID: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con ID: " + id));

        if (prestamo.getEstado() != EstadoPrestamo.REGISTRADO) {
            throw new ReglaNegocioException("Solo se pueden devolver prestamos en estado REGISTRADO.");
        }

        // Reponer Stock a los libros
        for (DetallePrestamo detalle : prestamo.getDetalles()) {
            Libro libro = detalle.getLibro();
            libro.setStock(libro.getStock() + detalle.getCantidad());
            libroRepository.save(libro);
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDate.now());

        return mapToResponseDTO(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public PrestamoResponseDTO anularPrestamo(Long id) {
        log.info("Anulando prestamo ID: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con ID: " + id));

        if (prestamo.getEstado() == EstadoPrestamo.ANULADO) {
            throw new ReglaNegocioException("El prestamo ya se encuentra anulado.");
        }

        // Si estaba registrado, reponer stock al anular
        if (prestamo.getEstado() == EstadoPrestamo.REGISTRADO) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                Libro libro = detalle.getLibro();
                libro.setStock(libro.getStock() + detalle.getCantidad());
                libroRepository.save(libro);
            }
        }

        prestamo.setEstado(EstadoPrestamo.ANULADO);

        return mapToResponseDTO(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> buscarConFiltros(Long socioId, EstadoPrestamo estado, LocalDateTime desde, LocalDateTime hasta, String orden) {
        Sort sort = "asc".equalsIgnoreCase(orden) ? Sort.by("fecha").ascending() : Sort.by("fecha").descending();
        return prestamoRepository.buscarFiltrosCombinados(socioId, estado, desde, hasta, sort)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private PrestamoResponseDTO mapToResponseDTO(Prestamo entity) {
        List<DetallePrestamoResponseDTO> detallesDTO = entity.getDetalles().stream()
                .map(d -> DetallePrestamoResponseDTO.builder()
                        .id(d.getId())
                        .libroId(d.getLibro().getId())
                        .titulo(d.getLibro().getTitulo())
                        .cantidad(d.getCantidad())
                        .costoUnitario(d.getCostoUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        return PrestamoResponseDTO.builder()
                .id(entity.getId())
                .fecha(entity.getFecha())
                .fechaDevolucionPrevista(entity.getFechaDevolucionPrevista())
                .fechaDevolucionReal(entity.getFechaDevolucionReal())
                .estado(entity.getEstado())
                .socioId(entity.getSocio().getId())
                .socioNombre(entity.getSocio().getNombres() + " " + entity.getSocio().getApellidos())
                .totalValorizado(entity.getTotalValorizado())
                .detalles(detallesDTO)
                .build();
    }
}