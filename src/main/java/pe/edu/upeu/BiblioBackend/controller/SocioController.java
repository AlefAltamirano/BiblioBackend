package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.SocioRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.SocioResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.SocioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    @PostMapping
    public ResponseEntity<SocioResponseDTO> create(@Valid @RequestBody SocioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(socioService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SocioRequestDTO request) {
        return ResponseEntity.ok(socioService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.read(id));
    }

    @GetMapping
    public ResponseEntity<List<SocioResponseDTO>> readAll() {
        return ResponseEntity.ok(socioService.readAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        socioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}