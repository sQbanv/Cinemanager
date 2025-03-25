package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
public class Genre {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64, unique = true)
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
