package org.ratifire.admin.carrentalsystem.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.entity.Car;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarConverter {

    public static CarDto toDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .carType(car.getCarType())
                .plateNumber(car.getPlateNumber())
                .build();
    }

    public static Car toEntity(CarDto dto) {
        return Car.builder()
                .carType(dto.getCarType())
                .plateNumber(dto.getPlateNumber())
                .build();
    }
}
