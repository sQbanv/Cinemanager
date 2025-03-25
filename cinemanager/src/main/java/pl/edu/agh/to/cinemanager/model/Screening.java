package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Getter
@Setter
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    @ManyToOne(optional = false)
    private Movie movie;

    @NotNull
    @ManyToOne(optional = false)
    private CinemaRoom cinemaRoom;

    @NotNull
    @ManyToOne(optional = false)
    private ScreeningType screeningType;

    public Screening(LocalDateTime startDate, Movie movie, CinemaRoom cinemaRoom, ScreeningType screeningType) {
        this.startDate = startDate;
        this.movie = movie;
        this.cinemaRoom = cinemaRoom;
        this.screeningType = screeningType;
    }
}
