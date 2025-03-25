package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.cinemanager.dto.ResponseTicketDto;
import pl.edu.agh.to.cinemanager.model.Ticket;
import pl.edu.agh.to.cinemanager.service.TicketService;

@RestController
@RequestMapping(path = "api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public Page<ResponseTicketDto> getTickets(Authentication authentication, Pageable pageable,
                                              @RequestParam(value = "past", defaultValue = "false", required = false) boolean past) {
        if (past) {
            return ticketService.getAllTicketsForCustomer(authentication.getName(), pageable);
        } else {
            return ticketService.getFutureTicketsForCustomer(authentication.getName(), pageable);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseTicketDto getTicket(Authentication authentication,
                                       @PathVariable("id") Ticket ticket) {
        return ticketService.getTicketByIdForCustomer(authentication.getName(), ticket);
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateTicket(@PathVariable("id") Ticket ticket) {
        ticketService.validateTicket(ticket);
    }
}
