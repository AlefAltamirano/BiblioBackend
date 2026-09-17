package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.BiblioBackend.entity.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}