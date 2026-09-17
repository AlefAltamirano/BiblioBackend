package pe.edu.upeu.BiblioBackend.service.service;

import pe.edu.upeu.BiblioBackend.dto.GeneroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.GeneroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.generic.CrudService;

public interface GeneroService extends CrudService<GeneroRequestDTO, GeneroResponseDTO, Long> {
}