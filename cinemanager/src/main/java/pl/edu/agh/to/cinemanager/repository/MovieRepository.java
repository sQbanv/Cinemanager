package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Movie;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    @Query("SELECT m FROM Movie m INNER JOIN Review r ON m = r.movie WHERE r.reviewDate BETWEEN :startDate AND :endDate GROUP BY r.movie ORDER BY avg(r.rating) DESC LIMIT 5")
    List<Movie> getPopularMoviesBetweenDates(@Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);
}
