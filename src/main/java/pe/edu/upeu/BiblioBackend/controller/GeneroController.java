package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.GeneroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.GeneroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.GeneroService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generos")
@RequiredArgsConstructor
public class GeneroController {

    private final GeneroService generoService;

    @PostMapping
    public ResponseEntity<GeneroResponseDTO> create(@Valid @RequestBody GeneroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generoService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody GeneroRequestDTO request) {
        return ResponseEntity.ok(generoService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.read(id));
    }

    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> readAll() {
        return ResponseEntity.ok(generoService.readAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}