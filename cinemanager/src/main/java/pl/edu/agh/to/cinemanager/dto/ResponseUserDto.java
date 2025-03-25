package pl.edu.agh.to.cinemanager.dto;

import pl.edu.agh.to.cinemanager.model.UserRole;

public record ResponseUserDto(long id, String firstName, String lastName, String email, UserRole role) {
}
