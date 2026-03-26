package org.ratifire.admin.carrentalsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto create(@RequestBody CarDto dto) {
        return carService.create(dto);
    }

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable Long id) {
        return carService.getById(id);
    }

    @GetMapping
    public List<CarDto> getByCarType(@RequestParam CarType type) {
        return carService.getByCarType(type);
    }
}
