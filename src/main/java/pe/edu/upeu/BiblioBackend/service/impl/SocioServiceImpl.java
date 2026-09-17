package pe.edu.upeu.BiblioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.SocioRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.SocioResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Socio;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.SocioRepository;
import pe.edu.upeu.BiblioBackend.service.service.SocioService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;

    @Override
    @Transactional
    public SocioResponseDTO create(SocioRequestDTO request) {
        log.info("Registrando nuevo socio con DNI: {}", request.getDni());

        if (socioRepository.existsByDni(request.getDni())) {
            throw new ReglaNegocioException("Ya existe un socio registrado con el DNI: " + request.getDni());
        }

        Socio socio = Socio.builder()
                .dni(request.getDni().trim())
                .nombres(request.getNombres().trim())
                .apellidos(request.getApellidos().trim())
                .email(request.getEmail().trim())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .build();

        return mapToResponseDTO(socioRepository.save(socio));
    }

    @Override
    @Transactional
    public SocioResponseDTO update(Long id, SocioRequestDTO request) {
        log.info("Actualizando datos del socio con ID: {}", id);

        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con ID: " + id));

        if (socioRepository.existsByDniAndIdNot(request.getDni(), id)) {
            throw new ReglaNegocioException("Ya existe otro socio registrado con el DNI: " + request.getDni());
        }

        socio.setDni(request.getDni().trim());
        socio.setNombres(request.getNombres().trim());
        socio.setApellidos(request.getApellidos().trim());
        socio.setEmail(request.getEmail().trim());
        socio.setTelefono(request.getTelefono());
        socio.setDireccion(request.getDireccion());
        socio.setEstado(request.getEstado());

        return mapToResponseDTO(socioRepository.save(socio));
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponseDTO read(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con ID: " + id));
        return mapToResponseDTO(socio);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando socio con ID: {}", id);

        if (!socioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Socio no encontrado con ID: " + id);
        }

        socioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponseDTO> readAll() {
        return socioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private SocioResponseDTO mapToResponseDTO(Socio entity) {
        return SocioResponseDTO.builder()
                .id(entity.getId())
                .dni(entity.getDni())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }
}