package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestMovieDto;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieDto;
import pl.edu.agh.to.cinemanager.dto.ResponseMoviesDto;
import pl.edu.agh.to.cinemanager.model.Director;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.repository.MovieRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final DirectorService directorService;

    public Page<ResponseMovieDto> getAllMovies(Specification<Movie> specification, Pageable pageable) {
        return movieRepository.findAll(
                        specification,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))))
                .map(this::movieToResponseDto);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public ResponseMoviesDto getMostPopularMoviesFromPreviousWeek(){
        LocalDateTime endDate = LocalDateTime.now();
        List<Movie> resposeData = movieRepository.getPopularMoviesBetweenDates(endDate.minusWeeks(1), endDate);
        return new ResponseMoviesDto(resposeData.stream().map(this::movieToResponseDto).toList());
    }

    public ResponseMovieDto createMovie(RequestMovieDto movieDto, MultipartFile poster) {
        String uploadDir = "src/main/resources/static/posters";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + poster.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            Files.copy(poster.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Movie movie = new Movie(
                movieDto.title(),
                movieDto.description(),
                getDirectorFromRequestDto(movieDto),
                "posters/" + fileName,
                movieDto.length(),
                getGenreFromRequestDto(movieDto)
        );

        System.out.println(movie.getPosterUrl());

        save(movie);

        return movieToResponseDto(movie);
    }

    public void updateMovie(Movie movie, RequestMovieDto updatedMovieDto) {
        movie.setTitle(updatedMovieDto.title());
        movie.setDescription(updatedMovieDto.description());
        movie.setDirector(getDirectorFromRequestDto(updatedMovieDto));
        movie.setPosterUrl(updatedMovieDto.posterUrl());
        movie.setLength(updatedMovieDto.length());
        movie.setGenre(getGenreFromRequestDto(updatedMovieDto));

        save(movie);
    }

    public ResponseMovieDto movieToResponseDto(Movie movie) {
        return new ResponseMovieDto(movie.getId(), movie.getTitle(), movie.getDescription(),
                directorService.directorToResponseDto(movie.getDirector()), movie.getPosterUrl(), movie.getLength(),
                genreService.genreToResponseDto(movie.getGenre()));
    }

    private Genre getGenreFromRequestDto(RequestMovieDto movieDto) {
        return genreService.getGenreById(movieDto.genreId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This genre does not exist"));
    }

    private Director getDirectorFromRequestDto(RequestMovieDto movieDto) {
        return directorService.getDirectorById(movieDto.directorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This director does not exist"));
    }

    private void save(Movie movie) {
        try {
            movieRepository.save(movie);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
