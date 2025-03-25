package pl.edu.agh.to.cinemanager.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.*;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.repository.specification.MovieSpecification;
import pl.edu.agh.to.cinemanager.service.MovieService;
import pl.edu.agh.to.cinemanager.service.ReviewService;
import pl.edu.agh.to.cinemanager.service.TicketService;

@RestController
@RequestMapping(path = "api/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final TicketService ticketService;

    @GetMapping("")
    public Page<ResponseMovieDto> getAllMovies(Pageable pageable,
                                               @RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "genre", required = false) String genre,
                                               @RequestParam(value = "minRating", required = false) Double minRating) {
        return movieService.getAllMovies(
                MovieSpecification.title(title)
                        .and(MovieSpecification.genre(genre))
                        .and(MovieSpecification.minRating(minRating)),
                pageable);
    }

    @GetMapping("/{id}")
    public ResponseMovieDto getMovieById(@PathVariable("id") Movie movie) {
        return movieService.movieToResponseDto(movie);
    }

    @GetMapping("/{id}/rating")
    public BigDecimal getMovieRating(@PathVariable("id") Movie movie) {
        return reviewService.getRating(movie);
    }

    @GetMapping("/popular")
    public ResponseMoviesDto getPopularMoviesFromPreviousWeek(){
        return movieService.getMostPopularMoviesFromPreviousWeek();
    }

    @GetMapping("/highest-rated")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseMovieRatingDto getHighestRatedMovies(){
        return reviewService.getHighestRatedMovies();
    }

    @GetMapping("/tickets-sold")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<ResponseMovieTicketsDto> getTicketsSold(@RequestParam(value = "after", required = false) LocalDateTime after,
                                                        @RequestParam(value = "before", required = false) LocalDateTime before) {
        return ticketService.getTicketsSold(after, before);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseMovieDto> createMovie(
            @RequestParam("movie") String movieJson,
            @RequestPart("poster") MultipartFile poster) {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestMovieDto movieDto;

        try {
            movieDto = objectMapper.readValue(movieJson, RequestMovieDto.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid movie data", e);
        }

        ResponseMovieDto responseMovieDto = movieService.createMovie(movieDto, poster);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/movies/{id}")
                .buildAndExpand(responseMovieDto.id()).toUri();

        return ResponseEntity.created(location).body(responseMovieDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(@PathVariable("id") Movie movie, @RequestBody RequestMovieDto updatedMovieDto) {
        movieService.updateMovie(movie, updatedMovieDto);
    }
}
