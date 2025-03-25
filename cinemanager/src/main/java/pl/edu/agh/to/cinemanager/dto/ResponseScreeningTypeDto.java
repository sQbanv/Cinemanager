package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record ResponseScreeningTypeDto(long id, String name, BigDecimal basePrice, BigDecimal discountPrice) {
}
