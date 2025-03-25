package pl.edu.agh.to.cinemanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record ResponseOrderDto(long id, LocalDateTime date, BigDecimal totalPrice, boolean paid, boolean cancelled,
                               List<ResponseTicketDto> tickets, Optional<Long> userId) {
}
