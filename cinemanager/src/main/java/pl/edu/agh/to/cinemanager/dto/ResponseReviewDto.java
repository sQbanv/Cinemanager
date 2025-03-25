package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseReviewDto(long id, long movieId, ResponseReviewUserDto user, BigDecimal rating, String content,
                                LocalDateTime reviewDate) {
}
