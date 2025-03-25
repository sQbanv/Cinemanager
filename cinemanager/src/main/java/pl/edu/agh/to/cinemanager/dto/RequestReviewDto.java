package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record RequestReviewDto(long movieId, BigDecimal rating, String content) {
}
