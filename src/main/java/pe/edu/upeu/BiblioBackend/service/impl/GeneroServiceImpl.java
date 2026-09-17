package pe.edu.upeu.BiblioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.GeneroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.GeneroResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Genero;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.GeneroRepository;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.service.service.GeneroService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepository;
    private final LibroRepository libroRepository;

    @Override
    @Transactional
    public GeneroResponseDTO create(GeneroRequestDTO request) {
        log.info("Creando nuevo genero literario: {}", request.getNombre());

        if (generoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ReglaNegocioException("Ya existe un genero literario con el nombre: " + request.getNombre());
        }

        Genero genero = Genero.builder()
                .nombre(request.getNombre().trim())
                .descripcion(request.getDescripcion())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .build();

        return mapToResponseDTO(generoRepository.save(genero));
    }

    @Override
    @Transactional
    public GeneroResponseDTO update(Long id, GeneroRequestDTO request) {
        log.info("Actualizando genero literario con ID: {}", id);

        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con ID: " + id));

        if (generoRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre(), id)) {
            throw new ReglaNegocioException("Ya existe otro genero literario registrado con el nombre: " + request.getNombre());
        }

        genero.setNombre(request.getNombre().trim());
        genero.setDescripcion(request.getDescripcion());
        genero.setEstado(request.getEstado());

        return mapToResponseDTO(generoRepository.save(genero));
    }

    @Override
    @Transactional(readOnly = true)
    public GeneroResponseDTO read(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con ID: " + id));
        return mapToResponseDTO(genero);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando genero con ID: {}", id);

        if (!generoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Genero no encontrado con ID: " + id);
        }

        if (libroRepository.existsByGeneroId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el genero porque existen libros asociados a este.");
        }

        generoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneroResponseDTO> readAll() {
        return generoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private GeneroResponseDTO mapToResponseDTO(Genero entity) {
        return GeneroResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }
}