package pl.edu.agh.to.cinemanager.dto;

import java.math.BigDecimal;

public record ScreeningAttendanceDto(ResponseScreeningDto screening, BigDecimal attendancePercentage) {
}
