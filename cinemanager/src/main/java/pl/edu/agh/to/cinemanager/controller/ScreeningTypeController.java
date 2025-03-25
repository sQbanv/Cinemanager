package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestScreeningTypeDto;
import pl.edu.agh.to.cinemanager.dto.ResponseScreeningTypeDto;
import pl.edu.agh.to.cinemanager.model.ScreeningType;
import pl.edu.agh.to.cinemanager.service.ScreeningTypeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/screening-types")
@AllArgsConstructor
public class ScreeningTypeController {

    private final ScreeningTypeService screeningTypeService;

    @GetMapping("")
    public List<ResponseScreeningTypeDto> getAllScreeningTypes() {
        return screeningTypeService.getAllScreeningTypes();
    }

    @GetMapping("/{id}")
    public ResponseScreeningTypeDto getScreeningTypeById(@PathVariable("id") ScreeningType screeningType) {
        return screeningTypeService.screeningTypeToScreeningTypeDto(screeningType);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResponseScreeningTypeDto> createScreeningType(@RequestBody RequestScreeningTypeDto requestScreeningTypeDto) {
        ResponseScreeningTypeDto responseScreeningTypeDto = screeningTypeService.createScreeningType(requestScreeningTypeDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/{id}")
                .buildAndExpand(requestScreeningTypeDto.id()).toUri();

        return ResponseEntity.created(location).body(responseScreeningTypeDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScreeningType(@PathVariable("id") ScreeningType screeningType, @RequestBody RequestScreeningTypeDto requestScreeningTypeDto) {
        screeningTypeService.updateScreeningType(screeningType, requestScreeningTypeDto);
    }

}
