package pl.edu.agh.to.cinemanager.dto;

import java.util.List;

public record RequestOrderDto(long screeningId, List<TicketDto> tickets) {
}
