package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue
    private long id;

    private int seatRow;

    private int seatPosition;

    private boolean used = false;
    
    private boolean discounted;

    @NotNull
    @ManyToOne(optional = false)
    private Screening screening;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    private Order order;

    public Ticket(int seatRow, int seatPosition, Screening screening, User user, Order order, boolean discounted) {
        this.seatRow = seatRow;
        this.seatPosition = seatPosition;
        this.screening = screening;
        this.user = user;
        this.order = order;
        this.discounted = discounted;
    }
}
