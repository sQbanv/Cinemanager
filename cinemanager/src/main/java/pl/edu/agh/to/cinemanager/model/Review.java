package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Digits(integer = 1, fraction = 1)
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @NotNull
    @Column(length = 1024)
    private String content;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    private Movie movie;

    @NotNull
    private LocalDateTime reviewDate;

    public Review(BigDecimal rating, String content, User user, Movie movie, LocalDateTime reviewDate) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.movie = movie;
        this.reviewDate = reviewDate;
    }
}
