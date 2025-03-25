package pl.edu.agh.to.cinemanager.dto;

public record ResponseMovieTicketsDto(ResponseMovieDto movie, long ticketsSold) {
}
