package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestOrderDto;
import pl.edu.agh.to.cinemanager.dto.RequestOrderUpdateDto;
import pl.edu.agh.to.cinemanager.dto.ResponseOrderDto;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.specification.OrderSpecification;
import pl.edu.agh.to.cinemanager.service.AuthService;
import pl.edu.agh.to.cinemanager.service.OrderService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public Page<ResponseOrderDto> getAllOrders(Authentication authentication, Pageable pageable,
                                               @RequestParam(value = "userId", required = false) User user,
                                               @RequestParam(value = "paid", required = false) Boolean paid,
                                               @RequestParam(value = "cancelled", required = false) Boolean cancelled,
                                               @RequestParam(value = "after", required = false) LocalDateTime after,
                                               @RequestParam(value = "before", required = false) LocalDateTime before) {
        Specification<Order> orderSpecification = OrderSpecification.user(user)
                .and(OrderSpecification.paid(paid))
                .and(OrderSpecification.cancelled(cancelled))
                .and(OrderSpecification.afterDate(after))
                .and(OrderSpecification.beforeDate(before));

        if (authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            return orderService.getAllOrders(orderSpecification, pageable);
        } else {
            return orderService.getOrdersForCustomer(orderSpecification, pageable, authentication.getName());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseOrderDto getOrder(Authentication authentication, @PathVariable("id") Order order) {
        if (authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            return orderService.orderToResponseDto(order, true);
        } else if (!Objects.equals(order.getUser().getEmail(), authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return orderService.orderToResponseDto(order, false);
        }
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseOrderDto> createOrder(Authentication authentication, @RequestBody RequestOrderDto orderDto){
        ResponseOrderDto response = orderService.createOrderForUserByEmail(authentication.getName(), orderDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/orders/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/payment")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePayment(@PathVariable("id") Order order) {
        orderService.updatePayment(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@PathVariable("id") Order order, @RequestBody RequestOrderUpdateDto updatedOrderDto) {
        orderService.updateOrder(order, updatedOrderDto);
    }
}
