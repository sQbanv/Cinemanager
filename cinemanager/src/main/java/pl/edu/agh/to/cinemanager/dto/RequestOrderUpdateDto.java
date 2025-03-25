package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RequestOrderUpdateDto(long userId, LocalDateTime date, BigDecimal totalPrice,
                                    boolean paid, boolean cancelled) {
}
