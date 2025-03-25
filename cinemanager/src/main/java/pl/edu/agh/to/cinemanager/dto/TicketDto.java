package pl.edu.agh.to.cinemanager.dto;

import pl.edu.agh.to.cinemanager.model.TicketType;

public record TicketDto(Integer row, Integer seatNumber, TicketType ticketType) {
}
