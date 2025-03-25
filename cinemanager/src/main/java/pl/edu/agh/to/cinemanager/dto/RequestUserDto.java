package pl.edu.agh.to.cinemanager.dto;

import pl.edu.agh.to.cinemanager.model.UserRole;

public record RequestUserDto(String firstName, String lastName, String email, String password, UserRole role) {
}
