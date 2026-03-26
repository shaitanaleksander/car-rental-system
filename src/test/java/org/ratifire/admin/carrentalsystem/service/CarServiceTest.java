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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void create_shouldSaveAndReturnDto() {
        CarDto dto = CarDto.builder()
                .carType(CarType.SEDAN)
                .plateNumber("AB-123-CD")
                .build();

        Car saved = Car.builder()
                .id(1L)
                .carType(CarType.SEDAN)
                .plateNumber("AB-123-CD")
                .build();

        when(carRepository.save(any(Car.class))).thenReturn(saved);

        CarDto result = carService.create(dto);

        assertEquals(1L, result.getId());
        assertEquals(CarType.SEDAN, result.getCarType());
        assertEquals("AB-123-CD", result.getPlateNumber());
        verify(carRepository).save(any(Car.class));
    }

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
    void getByCarType_shouldReturnFilteredList() {
        List<Car> sedans = List.of(
                Car.builder().id(1L).carType(CarType.SEDAN).plateNumber("AB-123-CD").build(),
                Car.builder().id(2L).carType(CarType.SEDAN).plateNumber("EF-456-GH").build()
        );

        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(sedans);

        List<CarDto> result = carService.getByCarType(CarType.SEDAN);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getCarType() == CarType.SEDAN));
    }

    @Test
    void getByCarType_shouldReturnEmptyList_whenNoneFound() {
        when(carRepository.findByCarType(CarType.VAN)).thenReturn(List.of());

        List<CarDto> result = carService.getByCarType(CarType.VAN);

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldDelete_whenExists() {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.delete(1L);

        verify(carRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(carRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> carService.delete(99L));
        verify(carRepository, never()).deleteById(any());
    }
}
