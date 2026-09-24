package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import pe.edu.upeu.BiblioBackend.entity.Prestamo;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // 1. Busqueda con filtros combinados opcionales y optimizacion JOIN FETCH
    @Query("""
    SELECT DISTINCT p FROM Prestamo p
    LEFT JOIN FETCH p.socio s
    LEFT JOIN FETCH p.detalles d
    LEFT JOIN FETCH d.libro l
    WHERE (:socioId IS NULL OR p.socio.id = :socioId)
      AND (:estado IS NULL OR p.estado = :estado)
      AND (:desde IS NULL OR p.fecha >= :desde)
      AND (:hasta IS NULL OR p.fecha <= :hasta)
    """)
    List<Prestamo> buscarFiltrosCombinados(
            @Param("socioId") Long socioId,
            @Param("estado") EstadoPrestamo estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Sort sort
    );

    // 2. Reporte: Prestamos por Genero (Excluyendo ANULADO)
    @Query("""
        SELECT new pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO(
            g.id, g.nombre, SUM(d.cantidad), SUM(d.subtotal)
        )
        FROM DetallePrestamo d
        JOIN d.libro l
        JOIN l.genero g
        JOIN d.prestamo p
        WHERE p.estado <> pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo.ANULADO
        AND (:desde IS NULL OR p.fecha >= :desde)
        AND (:hasta IS NULL OR p.fecha <= :hasta)
        GROUP BY g.id, g.nombre
        ORDER BY SUM(d.cantidad) DESC
        """)
    List<PrestamoPorGeneroDTO> reportePrestamosPorGenero(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    // 3. Reporte: Libros mas prestados (Excluyendo ANULADO)
    @Query("""
        SELECT new pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO(
            l.id, l.titulo, g.nombre, SUM(d.cantidad), SUM(d.subtotal)
        )
        FROM DetallePrestamo d
        JOIN d.libro l
        JOIN l.genero g
        JOIN d.prestamo p
        WHERE p.estado <> pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo.ANULADO
        AND (:desde IS NULL OR p.fecha >= :desde)
        AND (:hasta IS NULL OR p.fecha <= :hasta)
        GROUP BY l.id, l.titulo, g.nombre
        ORDER BY SUM(d.cantidad) DESC
        """)
    List<LibroMasPrestadoDTO> reporteLibrosMasPrestados(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}