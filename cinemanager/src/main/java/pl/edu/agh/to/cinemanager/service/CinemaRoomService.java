package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.RequestCinemaRoomDto;
import pl.edu.agh.to.cinemanager.dto.ResponseCinemaRoomDto;
import pl.edu.agh.to.cinemanager.dto.ResponseSeatStatisticsDto;
import pl.edu.agh.to.cinemanager.model.CinemaRoom;
import pl.edu.agh.to.cinemanager.repository.CinemaRoomRepository;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;
    private final TicketRepository ticketRepository;

    public List<ResponseCinemaRoomDto> getAllRooms() {
        return cinemaRoomRepository.findAll().stream().map(this::cinemaRoomToCinemaRoomResponseDto).toList();
    }

    public Optional<CinemaRoom> getCinemaRoomById(Long id) {
        return cinemaRoomRepository.findById(id);
    }

    public List<ResponseSeatStatisticsDto> getMostChosenSeats(CinemaRoom cinemaRoom) {
        return ticketRepository.findMostChosenSeats(cinemaRoom)
                .stream().map(data -> new ResponseSeatStatisticsDto((int)data[0], (int)data[1], (long)data[2]))
                .toList();
    }

    public ResponseCinemaRoomDto cinemaRoomToCinemaRoomResponseDto(CinemaRoom cinemaRoom) {
        return new ResponseCinemaRoomDto(cinemaRoom.getId(), cinemaRoom.getName(), cinemaRoom.getRows(), cinemaRoom.getSeatsPerRow());
    }

    public ResponseCinemaRoomDto createCinemaRoom(RequestCinemaRoomDto requestCinemaRoomDto) {
        CinemaRoom cinemaRoom = new CinemaRoom(requestCinemaRoomDto.name(), requestCinemaRoomDto.rows(), requestCinemaRoomDto.seatsPerRow());
        save(cinemaRoom);

        return cinemaRoomToCinemaRoomResponseDto(cinemaRoom);
    }

    public void updateCinemaRoom(CinemaRoom cinemaRoom, RequestCinemaRoomDto requestCinemaRoomDto) {
        cinemaRoom.setName(requestCinemaRoomDto.name());
        cinemaRoom.setRows(requestCinemaRoomDto.rows());
        cinemaRoom.setSeatsPerRow(requestCinemaRoomDto.seatsPerRow());

        save(cinemaRoom);
    }

    private void save(CinemaRoom cinemaRoom) {
        try {
            cinemaRoomRepository.save(cinemaRoom);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
