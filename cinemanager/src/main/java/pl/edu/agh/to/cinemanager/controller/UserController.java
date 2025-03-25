package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.cinemanager.dto.RequestUserDetailsChangeDto;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("")
    public List<ResponseUserDto> getAllUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseUserDto getUserById(@PathVariable("id") User user, Authentication authentication) {
        return userService.getUser(user, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") User user, Authentication authentication) {
        userService.deleteUser(user, authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void editUser(@PathVariable("id") User user, @RequestBody RequestUserDetailsChangeDto userDto, Authentication authentication){
        userService.updateUserDetails(user, userDto, authentication);
    }
}
