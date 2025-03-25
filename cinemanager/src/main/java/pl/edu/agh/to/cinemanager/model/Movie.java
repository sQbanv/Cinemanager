package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 128)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @ManyToOne(optional = false)
    private Director director;

    @NotBlank
    @Column(length = 256)
    private String posterUrl;

    private int length;

    @NotNull
    @ManyToOne(optional = false)
    private Genre genre;

    public Movie(String title, String description, Director director, String posterUrl, int length, Genre genre) {
        this.title = title;
        this.description = description;
        this.director = director;
        this.posterUrl = posterUrl;
        this.length = length;
        this.genre = genre;
    }
}
