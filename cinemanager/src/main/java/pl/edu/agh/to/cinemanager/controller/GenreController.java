package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestGenreDto;
import pl.edu.agh.to.cinemanager.dto.ResponseGenreDto;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.service.GenreService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("")
    public List<ResponseGenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseGenreDto getGenreById(@PathVariable("id") Genre genre) {
        return genreService.genreToResponseDto(genre);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseGenreDto> createGenre(@RequestBody RequestGenreDto genreDto) {
        ResponseGenreDto responseGenreDto = genreService.createGenre(genreDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/genres/{id}")
                .buildAndExpand(responseGenreDto.id()).toUri();

        return ResponseEntity.created(location).body(responseGenreDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGenre(@PathVariable("id") Genre genre, @RequestBody RequestGenreDto updatedGenreDto) {
        genreService.updateGenre(genre, updatedGenreDto);
    }
}
