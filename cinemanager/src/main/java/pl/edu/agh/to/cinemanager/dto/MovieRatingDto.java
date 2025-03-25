package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record MovieRatingDto(ResponseMovieDto movie, BigDecimal rating) {
}
