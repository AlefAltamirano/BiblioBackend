package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;
import pe.edu.upeu.BiblioBackend.service.service.PrestamoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrarPrestamo(@Valid @RequestBody PrestamoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.registrarPrestamo(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponseDTO> registrarDevolucion(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<PrestamoResponseDTO> anularPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.anularPrestamo(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarConFiltros(
            @RequestParam(required = false) Long socioId,
            @RequestParam(required = false) EstadoPrestamo estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false, defaultValue = "desc") String orden
    ) {
        return ResponseEntity.ok(prestamoService.buscarConFiltros(socioId, estado, desde, hasta, orden));
    }
}