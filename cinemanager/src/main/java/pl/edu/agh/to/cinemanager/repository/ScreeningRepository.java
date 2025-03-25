package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.CinemaRoom;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Screening;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long>, JpaSpecificationExecutor<Screening> {
    Page<Screening> findAllByMovie(Movie movie, Pageable pageable);

    @Query("SELECT s from Screening s where s.cinemaRoom = ?1 and abs(day(s.startDate)-day(?2)) <= 1")
    List<Screening> findScreeningsInCinemaRoomAtMostOneDayAway(CinemaRoom cinemaRoom, LocalDateTime date);
}
