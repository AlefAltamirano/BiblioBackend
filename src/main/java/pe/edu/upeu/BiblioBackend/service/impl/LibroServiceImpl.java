package pe.edu.upeu.BiblioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.LibroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.LibroResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Genero;
import pe.edu.upeu.BiblioBackend.entity.Libro;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.DetallePrestamoRepository;
import pe.edu.upeu.BiblioBackend.repository.GeneroRepository;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.service.service.LibroService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final GeneroRepository generoRepository;
    private final DetallePrestamoRepository detallePrestamoRepository;

    @Override
    @Transactional
    public LibroResponseDTO create(LibroRequestDTO request) {
        log.info("Registrando nuevo libro: {}", request.getTitulo());

        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new ReglaNegocioException("Ya existe un libro registrado con el ISBN: " + request.getIsbn());
        }

        Genero genero = generoRepository.findById(request.getGeneroId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con ID: " + request.getGeneroId()));

        if (!genero.getEstado()) {
            throw new ReglaNegocioException("No se puede registrar un libro en un genero inactivo.");
        }

        Libro libro = Libro.builder()
                .titulo(request.getTitulo().trim())
                .autor(request.getAutor().trim())
                .isbn(request.getIsbn().trim())
                .costoReposicion(request.getCostoReposicion())
                .stock(request.getStock())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .genero(genero)
                .build();

        return mapToResponseDTO(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public LibroResponseDTO update(Long id, LibroRequestDTO request) {
        log.info("Actualizando libro con ID: {}", id);

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con ID: " + id));

        if (libroRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new ReglaNegocioException("Ya existe otro libro registrado con el ISBN: " + request.getIsbn());
        }

        Genero genero = generoRepository.findById(request.getGeneroId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con ID: " + request.getGeneroId()));

        libro.setTitulo(request.getTitulo().trim());
        libro.setAutor(request.getAutor().trim());
        libro.setIsbn(request.getIsbn().trim());
        libro.setCostoReposicion(request.getCostoReposicion());
        libro.setStock(request.getStock());
        libro.setEstado(request.getEstado());
        libro.setGenero(genero);

        return mapToResponseDTO(libroRepository.save(libro));
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponseDTO read(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con ID: " + id));
        return mapToResponseDTO(libro);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando libro con ID: {}", id);

        if (!libroRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Libro no encontrado con ID: " + id);
        }

        if (detallePrestamoRepository.existsByLibroId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el libro porque se encuentra asociado a prestamos registrados.");
        }

        libroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> readAll() {
        return libroRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private LibroResponseDTO mapToResponseDTO(Libro entity) {
        return LibroResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .autor(entity.getAutor())
                .isbn(entity.getIsbn())
                .costoReposicion(entity.getCostoReposicion())
                .stock(entity.getStock())
                .estado(entity.getEstado())
                .generoId(entity.getGenero().getId())
                .generoNombre(entity.getGenero().getNombre())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }
}