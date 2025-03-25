package pl.edu.agh.to.cinemanager.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record ResponseScreeningDto(long id, LocalDateTime startDate, ResponseScreeningTypeDto screeningType,
                                   ResponseMovieDto movie, ResponseCinemaRoomDto cinemaRoom) {
}
