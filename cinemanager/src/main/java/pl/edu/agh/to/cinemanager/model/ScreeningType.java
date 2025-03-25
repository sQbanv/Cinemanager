package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "screening_type")
@Getter
@Setter
@NoArgsConstructor
public class ScreeningType {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 32, unique = true)
    private String name;

    @NotNull
    private BigDecimal basePrice;

    @NotNull
    private BigDecimal discountPrice;

    public ScreeningType(String name, BigDecimal basePrice, BigDecimal discountPrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.discountPrice = discountPrice;
    }
}
