package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.LibroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.LibroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(@Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.ok(libroService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.read(id));
    }

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> readAll() {
        return ResponseEntity.ok(libroService.readAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}