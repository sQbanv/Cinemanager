package pl.edu.agh.to.cinemanager.dto;

public record ResponseTicketDto(Long id, Integer seatRow, Integer seatPosition, boolean used,
                                ResponseScreeningDto screening, boolean discounted) {
}
