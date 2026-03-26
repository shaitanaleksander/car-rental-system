package org.ratifire.admin.carrentalsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto create(@RequestBody ReservationDto dto) {
        return reservationService.create(dto);
    }

    @GetMapping
    public List<ReservationDto> getByUserId(@RequestParam Long userId) {
        return reservationService.getByUserId(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}
