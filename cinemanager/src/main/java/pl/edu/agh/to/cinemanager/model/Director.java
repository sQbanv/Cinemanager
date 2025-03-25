package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
public class Director {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64)
    private String firstName;

    @NotBlank
    @Column(length = 64)
    private String lastName;

    public Director(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
