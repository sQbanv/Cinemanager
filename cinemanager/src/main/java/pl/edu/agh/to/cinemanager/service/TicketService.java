package pl.edu.agh.to.cinemanager.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.ResponseMovieTicketsDto;
import pl.edu.agh.to.cinemanager.dto.ResponseTicketDto;
import pl.edu.agh.to.cinemanager.dto.TicketDto;
import pl.edu.agh.to.cinemanager.model.*;
import pl.edu.agh.to.cinemanager.repository.TicketRepository;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningService screeningService;
    private final UserRepository userRepository;
    private final MovieService movieService;

    @Transactional
    public void createTicketsFromOrderInformation(List<TicketDto> ticketDtos, Order order, Screening screening, User user){
        Iterator<TicketDto> ticketIterator = ticketDtos.iterator();
        List<Ticket> tickets = new ArrayList<>();
        while (ticketIterator.hasNext()){
            TicketDto ticketDto = ticketIterator.next();
            int row = ticketDto.row();
            int seatNumber = ticketDto.seatNumber();
            if (ticketRepository.findByScreeningAndSeatRowAndSeatPositionAndOrderCancelledFalse(screening,row,seatNumber).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the tickets is already taken");
            }

            boolean discounted = ticketDto.ticketType().equals(TicketType.DISCOUNTED);
            Ticket ticket = new Ticket(row, seatNumber, screening, user, order, discounted);

            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }

    public List<Object[]> getAmountOfTicketsBoughtPerScreening(){
        return ticketRepository.getScreeningsWithNumberOfTicketsBought();
    }

    public Page<ResponseTicketDto> getFutureTicketsForCustomer(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));

        LocalDateTime now = LocalDateTime.now().minusHours(2);

        return ticketRepository.findByOrderPaidTrueAndOrderCancelledFalseAndScreeningStartDateAfterAndUserAndUsedFalse(now, user, pageable)
                .map(this::ticketToResponseDto);
    }

    public Page<ResponseTicketDto> getAllTicketsForCustomer(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist"));

        return ticketRepository.findByOrderPaidTrueAndOrderCancelledFalseAndUser(user, pageable)
                .map(this::ticketToResponseDto);
    }

    public List<ResponseMovieTicketsDto> getTicketsSold(LocalDateTime after, LocalDateTime before) {
        return ticketRepository.findTicketsSold(after, before)
                .stream().map(data -> new ResponseMovieTicketsDto(
                        movieService.movieToResponseDto((Movie)data[0]), (long)data[1])).toList();
    }

    public ResponseTicketDto getTicketByIdForCustomer(String email, Ticket ticket) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exit"));

        return ticketRepository.findTicketByIdAndUser(ticket.getId(), user)
                .map(this::ticketToResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket with id " + ticket.getId() + " does not exist or is not yours"));
    }

    public void validateTicket(Ticket ticket) {
        if (!ticket.getOrder().isPaid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order for this ticket has not been paid");
        } else if (ticket.getOrder().isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order for this ticket has been cancelled");
        }
        else if (ticket.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket has already been used");
        } else {
            ticket.setUsed(true);
            ticketRepository.save(ticket);
        }
    }

    public ResponseTicketDto ticketToResponseDto(Ticket ticket) {
        return new ResponseTicketDto(ticket.getId(), ticket.getSeatRow(), ticket.getSeatPosition(), ticket.isUsed(),
                screeningService.screeningToScreeningDto(ticket.getScreening()), ticket.isDiscounted());
    }
}
