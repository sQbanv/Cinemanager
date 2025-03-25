package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.*;
import pl.edu.agh.to.cinemanager.model.*;
import pl.edu.agh.to.cinemanager.repository.ScreeningRepository;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningTypeService screeningTypeService;
    private final MovieService movieService;
    private final CinemaRoomService cinemaRoomService;
    private final TicketRepository ticketRepository;

    public Page<ResponseScreeningDto> getAllScreenings(Specification<Screening> specification, Pageable pageable) {
        return screeningRepository.findAll(
                        specification,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "startDate")))
                )
                .map(this::screeningToScreeningDto);
    }

    public ResponseScreeningDto screeningToScreeningDto(Screening screening) {
        return new ResponseScreeningDto(screening.getId(), screening.getStartDate(),
                screeningTypeService.screeningTypeToScreeningTypeDto(screening.getScreeningType()),
                movieService.movieToResponseDto(screening.getMovie()),
                cinemaRoomService.cinemaRoomToCinemaRoomResponseDto(screening.getCinemaRoom()));
    }

    public ResponseScreeningDto createScreening(RequestScreeningDto screeningDto) {
        Screening screening = new Screening(screeningDto.startDate(),
                getMovieFromRequestDto(screeningDto),
                getCinemaRoomFromRequestDto(screeningDto),
                getScreeningTypeFromRequestDto(screeningDto));

        validateAndSave(screening);

        return screeningToScreeningDto(screening);
    }

    public void updateScreening(Screening screening, RequestScreeningDto screeningDto) {
        screening.setScreeningType(getScreeningTypeFromRequestDto(screeningDto));
        screening.setMovie(getMovieFromRequestDto(screeningDto));
        screening.setCinemaRoom(getCinemaRoomFromRequestDto(screeningDto));
        screening.setStartDate(screeningDto.startDate());

        validateAndSave(screening);
    }

    public List<ResponseTakenSeatDto> getTakenSeats(Screening screening) {
        return ticketRepository.findAllByScreeningAndOrderCancelledFalse(screening).stream()
                .map(this::getTakenSeatDtoFromTicket)
                .toList();
    }

    public ResponseScreeningAttendanceDto getScreeningsWithHighestAttendance(Integer no_entries){
        List<Object[]> data = ticketRepository.getScreeningsWithNumberOfTicketsBought();
        List<ScreeningAttendanceDto> responseData = new ArrayList<>();
        for(Object[] o : data){
            Screening screening = (Screening) o[0];
            double amount = (double) ((long) o[1]);
            double roomSize = (double) screening.getCinemaRoom().getRows() * screening.getCinemaRoom().getSeatsPerRow();
            responseData.add( new ScreeningAttendanceDto(screeningToScreeningDto(screening), BigDecimal.valueOf(amount/roomSize).setScale(3, RoundingMode.HALF_UP)));
        }
        responseData.sort(Comparator.comparing(ScreeningAttendanceDto::attendancePercentage).reversed());
        return new ResponseScreeningAttendanceDto(responseData.subList(0, no_entries));
    }

    public Optional<Screening> getScreeningById(Long id){
        return screeningRepository.findById(id);
    }

    private Movie getMovieFromRequestDto(RequestScreeningDto screeningDto) {
        return movieService.getMovieById(screeningDto.movieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie does not exist"));
    }

    private CinemaRoom getCinemaRoomFromRequestDto(RequestScreeningDto screeningDto) {
        return cinemaRoomService.getCinemaRoomById(screeningDto.cinemaRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This room does not exist"));
    }

    private ScreeningType getScreeningTypeFromRequestDto(RequestScreeningDto screeningDto) {
        return screeningTypeService.getScreeningTypeById(screeningDto.screeningTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This screening type does not exist"));
    }

    private ResponseTakenSeatDto getTakenSeatDtoFromTicket(Ticket ticket) {
        return new ResponseTakenSeatDto(ticket.getSeatRow(), ticket.getSeatPosition());
    }

    private boolean isValidScreening(Screening screeningToValidate) {
        LocalDateTime startDate = screeningToValidate.getStartDate();
        LocalDateTime endDate = startDate.plusMinutes(screeningToValidate.getMovie().getLength());

        List<Screening> possibleInterferenceScreenings = screeningRepository
                .findScreeningsInCinemaRoomAtMostOneDayAway(screeningToValidate.getCinemaRoom(),
                        screeningToValidate.getStartDate());

        return possibleInterferenceScreenings.stream().noneMatch(innerScreening -> {
            LocalDateTime innerStartDate = innerScreening.getStartDate();
            LocalDateTime innerEndDate = innerStartDate.plusMinutes(innerScreening.getMovie().getLength());

            boolean differentId = innerScreening.getId() != screeningToValidate.getId();
            boolean startsAndEndsBefore = innerStartDate.isBefore(startDate) && innerEndDate.isBefore(startDate);
            boolean startsAndEndsAfter = innerStartDate.isAfter(endDate) && innerEndDate.isAfter(endDate);

            return differentId && !startsAndEndsBefore && !startsAndEndsAfter;
        });
    }

    private void validateAndSave(Screening screening) {
        try {
            if (!isValidScreening(screening)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This screening overlaps with another screening");
            }
            screeningRepository.save(screening);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
