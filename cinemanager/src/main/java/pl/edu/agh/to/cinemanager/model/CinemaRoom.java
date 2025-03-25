package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "cinema_rooms")
@Getter
@Setter
@NoArgsConstructor
public class CinemaRoom {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 32, unique = true)
    private String name;

    private int rows;

    private int seatsPerRow;

    public CinemaRoom(String name, int rows, int seatsPerRow) {
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }
}
