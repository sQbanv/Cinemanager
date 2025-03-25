package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestUserDto;
import pl.edu.agh.to.cinemanager.dto.ResponseUserDto;
import pl.edu.agh.to.cinemanager.service.TokenService;
import pl.edu.agh.to.cinemanager.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/token")
    public String token(Authentication authentication) {
        try {
            log.debug("Token requested for user: '{}'", authentication.getName());
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> register(Authentication authentication, @RequestBody RequestUserDto requestUserDto) {
        ResponseUserDto responseUserDto = userService.registerUser(requestUserDto, authentication);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/users/{id}")
                .buildAndExpand(responseUserDto.id()).toUri();

        return ResponseEntity.created(location).body(responseUserDto);
    }
}
