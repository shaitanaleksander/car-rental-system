package org.ratifire.admin.carrentalsystem.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.ratifire.admin.carrentalsystem.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationConverter {

    public static ReservationDto toDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .carId(reservation.getCar().getId())
                .startDateTime(reservation.getStartDateTime())
                .days(reservation.getDays())
                .build();
    }

    public static Reservation toEntity(ReservationDto dto, User user, Car car) {
        return Reservation.builder()
                .user(user)
                .car(car)
                .startDateTime(dto.getStartDateTime())
                .days(dto.getDays())
                .build();
    }
}
