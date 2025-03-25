package pl.edu.agh.to.cinemanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieTicketsDto;
import pl.edu.agh.to.cinemanager.model.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByScreeningAndOrderCancelledFalse(Screening screening);

    Set<Ticket> findAllByOrder(Order order);

    Optional<Ticket> findByScreeningAndSeatRowAndSeatPositionAndOrderCancelledFalse(Screening screening, int row, int position);

    Page<Ticket> findByOrderPaidTrueAndOrderCancelledFalseAndScreeningStartDateAfterAndUserAndUsedFalse(
            LocalDateTime now, User user, Pageable pageable
    );

    Page<Ticket> findByOrderPaidTrueAndOrderCancelledFalseAndUser(User user, Pageable pageable);

    Optional<Ticket> findTicketByIdAndUser(long ticketId, User user);

    @Query("SELECT t.screening, count(t) FROM Ticket t WHERE t.used = TRUE GROUP BY t.screening")
    List<Object[]> getScreeningsWithNumberOfTicketsBought();

    @Query("SELECT m, COUNT(t.id) AS tickets " +
            "FROM Ticket t " +
            "INNER JOIN t.screening s " +
            "INNER JOIN s.movie m " +
            "INNER JOIN t.order o " +
            "WHERE o.paid = true AND o.cancelled = false " +
            "AND (:startDate is null or s.startDate >= :startDate) " +
            "AND (:endDate is null or s.startDate <= :endDate) " +
            "GROUP BY m.title " +
            "ORDER BY tickets DESC")
    List<Object[]> findTicketsSold(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.seatRow, t.seatPosition, COUNT(t.id) AS tickets " +
            "FROM Ticket t " +
            "INNER JOIN t.screening s " +
            "INNER JOIN s.cinemaRoom cr " +
            "INNER JOIN t.order o " +
            "WHERE o.cancelled = false AND o.paid = true AND cr = :cinemaRoom " +
            "GROUP BY t.seatRow, t.seatPosition")
    List<Object[]> findMostChosenSeats(@Param("cinemaRoom") CinemaRoom cinemaRoom);
}
