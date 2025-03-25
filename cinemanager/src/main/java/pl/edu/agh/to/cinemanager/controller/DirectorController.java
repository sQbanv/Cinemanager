package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestDirectorDto;
import pl.edu.agh.to.cinemanager.dto.ResponseDirectorDto;
import pl.edu.agh.to.cinemanager.model.Director;
import pl.edu.agh.to.cinemanager.service.DirectorService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/directors")
@AllArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("")
    public List<ResponseDirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public ResponseDirectorDto getDirectorById(@PathVariable("id") Director director) {
        return directorService.directorToResponseDto(director);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseDirectorDto> createDirector(@RequestBody RequestDirectorDto directorDto) {
        ResponseDirectorDto responseDirectorDto = directorService.createDirector(directorDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/directors/{id}")
                .buildAndExpand(responseDirectorDto.id()).toUri();

        return ResponseEntity.created(location).body(responseDirectorDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDirector(@PathVariable("id") Director director, @RequestBody RequestDirectorDto updatedDirectorDto) {
        directorService.updateDirector(director, updatedDirectorDto);
    }
}
