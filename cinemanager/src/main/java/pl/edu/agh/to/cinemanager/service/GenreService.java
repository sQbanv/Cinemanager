package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestGenreDto;
import pl.edu.agh.to.cinemanager.dto.ResponseGenreDto;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<ResponseGenreDto> getAllGenres() {
        return genreRepository.findAll().stream().map(this::genreToResponseDto).toList();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public ResponseGenreDto createGenre(RequestGenreDto requestGenreDto) {
        Genre genre = new Genre(requestGenreDto.name());
        genreRepository.save(genre);

        return genreToResponseDto(genre);
    }

    public void updateGenre(Genre genre, RequestGenreDto updatedGenreDto) {
        genre.setName(updatedGenreDto.name());

        save(genre);
    }

    public ResponseGenreDto genreToResponseDto(Genre genre) {
        return new ResponseGenreDto(genre.getId(), genre.getName());
    }

    private void save(Genre genre) {
        try {
            genreRepository.save(genre);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
