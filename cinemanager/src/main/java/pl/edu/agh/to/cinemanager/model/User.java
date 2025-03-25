package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64)
    private String firstName;

    @NotBlank
    @Column(length = 64)
    private String lastName;

    @Email
    @Column(length = 128, unique = true)
    private String email;

    @NotBlank
    @Column(length = 256)
    private String password;

    @NotNull
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
