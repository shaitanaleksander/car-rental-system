package org.ratifire.admin.carrentalsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.exception.ResourceNotFoundException;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void getById_shouldReturnDto_whenFound() {
        Car car = Car.builder()
                .id(1L)
                .carType(CarType.SUV)
                .plateNumber("EF-456-GH")
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        CarDto result = carService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals(CarType.SUV, result.getCarType());
        assertEquals("EF-456-GH", result.getPlateNumber());
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.getById(99L));
    }

    @Test
    void getAll_shouldReturnLimitedList() {
        List<Car> cars = List.of(
                Car.builder().id(1L).carType(CarType.SEDAN).plateNumber("AB-123-CD").build(),
                Car.builder().id(2L).carType(CarType.SUV).plateNumber("EF-456-GH").build()
        );

        when(carRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(cars));

        List<CarDto> result = carService.getAll(5);

        assertEquals(2, result.size());
    }

    @Test
    void getByCarType_shouldReturnFilteredLimitedList() {
        List<Car> sedans = List.of(
                Car.builder().id(1L).carType(CarType.SEDAN).plateNumber("AB-123-CD").build(),
                Car.builder().id(2L).carType(CarType.SEDAN).plateNumber("EF-456-GH").build()
        );

        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(sedans);

        List<CarDto> result = carService.getByCarType(CarType.SEDAN, 5);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getCarType() == CarType.SEDAN));
    }

    @Test
    void getByCarType_shouldReturnEmptyList_whenNoneFound() {
        when(carRepository.findByCarType(CarType.VAN)).thenReturn(List.of());

        List<CarDto> result = carService.getByCarType(CarType.VAN, 5);

        assertTrue(result.isEmpty());
    }
}
