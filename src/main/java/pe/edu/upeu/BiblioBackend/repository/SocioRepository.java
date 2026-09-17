package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.BiblioBackend.entity.Socio;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {
    boolean existsByDni(String dni);
    boolean existsByDniAndIdNot(String dni, Long id);
}