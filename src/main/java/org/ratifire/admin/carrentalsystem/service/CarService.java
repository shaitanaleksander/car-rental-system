package org.ratifire.admin.carrentalsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ratifire.admin.carrentalsystem.converter.CarConverter;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.exception.ResourceNotFoundException;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public CarDto getById(Long id) {
        var car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));
        return CarConverter.toDto(car);
    }

    public List<CarDto> getAll(int limit) {
        return carRepository.findAll(PageRequest.of(0, limit)).stream()
                .map(CarConverter::toDto)
                .toList();
    }

    public List<CarDto> getByCarType(CarType carType, int limit) {
        return carRepository.findByCarType(carType).stream()
                .limit(limit)
                .map(CarConverter::toDto)
                .toList();
    }
}
