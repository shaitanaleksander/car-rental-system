package org.ratifire.admin.carrentalsystem.converter;

import org.junit.jupiter.api.Test;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.enums.CarType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationConverterTest {

    @Test
    void toDto_shouldMapAllFields() {
        User user = User.builder().id(1L).build();
        Car car = Car.builder().id(2L).build();
        LocalDateTime startDateTime = LocalDateTime.of(2026, 4, 1, 9, 0);

        Reservation reservation = Reservation.builder()
                .id(10L)
                .user(user)
                .car(car)
                .startDateTime(startDateTime)
                .days(3)
                .build();

        ReservationDto dto = ReservationConverter.toDto(reservation);

        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals(2L, dto.getCarId());
        assertEquals(startDateTime, dto.getStartDateTime());
        assertEquals(3, dto.getDays());
    }

    @Test
    void toEntity_shouldMapFieldsWithoutId() {
        User user = User.builder().id(1L).name("John").build();
        Car car = Car.builder().id(2L).carType(CarType.SEDAN).build();
        LocalDateTime startDateTime = LocalDateTime.of(2026, 4, 5, 14, 0);

        ReservationDto dto = ReservationDto.builder()
                .id(99L)
                .userId(1L)
                .carId(2L)
                .startDateTime(startDateTime)
                .days(5)
                .build();

        Reservation reservation = ReservationConverter.toEntity(dto, user, car);

        assertNull(reservation.getId());
        assertEquals(user, reservation.getUser());
        assertEquals(car, reservation.getCar());
        assertEquals(startDateTime, reservation.getStartDateTime());
        assertEquals(5, reservation.getDays());
    }
}
