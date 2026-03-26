package org.ratifire.admin.carrentalsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private static final int CAR_LIST_LIMIT = 5;

    private final CarService carService;

    @GetMapping
    public List<CarDto> getCars(@RequestParam(required = false) CarType type) {
        if (type != null) {
            return carService.getByCarType(type, CAR_LIST_LIMIT);
        }
        return carService.getAll(CAR_LIST_LIMIT);
    }

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable Long id) {
        return carService.getById(id);
    }
}
