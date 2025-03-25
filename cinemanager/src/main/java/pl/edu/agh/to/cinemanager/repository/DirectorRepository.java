package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.to.cinemanager.model.Director;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}
