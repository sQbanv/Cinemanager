package pl.edu.agh.to.cinemanager.dto;

import java.util.List;

public record ResponseMoviesDto(List<ResponseMovieDto> movies) {
}
