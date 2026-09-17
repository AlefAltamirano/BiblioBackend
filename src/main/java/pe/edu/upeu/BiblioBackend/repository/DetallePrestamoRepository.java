package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.BiblioBackend.entity.DetallePrestamo;

@Repository
public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {
    boolean existsByLibroId(Long libroId);
}