package org.ratifire.admin.carrentalsystem.converter;

import org.junit.jupiter.api.Test;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.enums.CarType;

import static org.junit.jupiter.api.Assertions.*;

class CarConverterTest {

    @Test
    void toDto_shouldMapAllFields() {
        Car car = Car.builder()
                .id(1L)
                .carType(CarType.SUV)
                .plateNumber("AB-123-CD")
                .build();

        CarDto dto = CarConverter.toDto(car);

        assertEquals(1L, dto.getId());
        assertEquals(CarType.SUV, dto.getCarType());
        assertEquals("AB-123-CD", dto.getPlateNumber());
    }

    @Test
    void toEntity_shouldMapFieldsWithoutId() {
        CarDto dto = CarDto.builder()
                .id(3L)
                .carType(CarType.VAN)
                .plateNumber("EF-456-GH")
                .build();

        Car car = CarConverter.toEntity(dto);

        assertNull(car.getId());
        assertEquals(CarType.VAN, car.getCarType());
        assertEquals("EF-456-GH", car.getPlateNumber());
    }
}
