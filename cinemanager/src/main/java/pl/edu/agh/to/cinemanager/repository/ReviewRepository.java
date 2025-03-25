package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    @Query("SELECT avg(r.rating) FROM Review r WHERE r.movie = ?1")
    BigDecimal getAverageRatingByMovie(Movie movie);

    @Query("SELECT r.movie, avg(r.rating) AS average FROM Review r INNER JOIN r.movie GROUP BY r.movie ORDER BY average DESC LIMIT 10")
    List<Object[]> getHighestRankingMovies();
}
