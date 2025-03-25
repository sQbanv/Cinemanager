package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private BigDecimal totalPrice;

    private boolean paid = false;

    private boolean cancelled = false;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

    public Order(LocalDateTime date, BigDecimal totalPrice, User user) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.user = user;
    }
}
