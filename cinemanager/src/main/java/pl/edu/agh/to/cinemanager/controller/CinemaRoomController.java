package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestCinemaRoomDto;
import pl.edu.agh.to.cinemanager.dto.ResponseCinemaRoomDto;
import pl.edu.agh.to.cinemanager.dto.ResponseSeatStatisticsDto;
import pl.edu.agh.to.cinemanager.model.CinemaRoom;
import pl.edu.agh.to.cinemanager.service.CinemaRoomService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/cinema-rooms")
@AllArgsConstructor
public class CinemaRoomController {

    private final CinemaRoomService cinemaRoomService;

    @GetMapping("")
    public List<ResponseCinemaRoomDto> getAllCinemaRooms() {
        return cinemaRoomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseCinemaRoomDto getCinemaRoomById(@PathVariable("id") CinemaRoom cinemaRoom) {
        return cinemaRoomService.cinemaRoomToCinemaRoomResponseDto(cinemaRoom);
    }

    @GetMapping("/{id}/most-chosen-seats")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<ResponseSeatStatisticsDto> getMostChosenSeats(@PathVariable("id") CinemaRoom cinemaRoom) {
        return cinemaRoomService.getMostChosenSeats(cinemaRoom);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseCinemaRoomDto> createCinemaRoom(@RequestBody RequestCinemaRoomDto requestCinemaRoomDto) {
        ResponseCinemaRoomDto responseCinemaRoomDto = cinemaRoomService.createCinemaRoom(requestCinemaRoomDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/cinema-rooms/{id}")
                .buildAndExpand(responseCinemaRoomDto.id()).toUri();

        return ResponseEntity.created(location).body(responseCinemaRoomDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCinemaRoom(@PathVariable("id") CinemaRoom cinemaRoom, @RequestBody RequestCinemaRoomDto updatedCinemaRoomDto) {
        cinemaRoomService.updateCinemaRoom(cinemaRoom, updatedCinemaRoomDto);
    }
}
