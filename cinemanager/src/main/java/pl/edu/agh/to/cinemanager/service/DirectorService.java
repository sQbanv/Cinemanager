package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestDirectorDto;
import pl.edu.agh.to.cinemanager.dto.ResponseDirectorDto;
import pl.edu.agh.to.cinemanager.model.Director;
import pl.edu.agh.to.cinemanager.repository.DirectorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public List<ResponseDirectorDto> getAllDirectors() {
        return directorRepository.findAll().stream().map(this::directorToResponseDto).toList();
    }

    public Optional<Director> getDirectorById(Long id) {
        return directorRepository.findById(id);
    }

    public ResponseDirectorDto createDirector(RequestDirectorDto requestDirectorDto) {
        Director director = new Director(requestDirectorDto.firstName(), requestDirectorDto.lastName());
        directorRepository.save(director);

        return directorToResponseDto(director);
    }

    public void updateDirector(Director director, RequestDirectorDto updatedDirectorDto) {
        director.setFirstName(updatedDirectorDto.firstName());
        director.setLastName(updatedDirectorDto.lastName());

        save(director);
    }

    public ResponseDirectorDto directorToResponseDto(Director director) {
        return new ResponseDirectorDto(director.getId(), director.getFirstName(), director.getLastName());
    }

    private void save(Director director) {
        try {
            directorRepository.save(director);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
