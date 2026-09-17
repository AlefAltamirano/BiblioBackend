package pe.edu.upeu.BiblioBackend.service.service;

import pe.edu.upeu.BiblioBackend.dto.LibroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.LibroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.generic.CrudService;

public interface LibroService extends CrudService<LibroRequestDTO, LibroResponseDTO, Long> {
}