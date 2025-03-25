package pl.edu.agh.to.cinemanager.dto;

public record ResponseMovieDto(long id, String title, String description, ResponseDirectorDto director,
                               String posterUrl, int length, ResponseGenreDto genre) {
}
