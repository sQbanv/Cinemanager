package pl.edu.agh.to.cinemanager.dto;

public record RequestMovieDto(String title, String description, long directorId,
                              String posterUrl, int length, long genreId) {
}
