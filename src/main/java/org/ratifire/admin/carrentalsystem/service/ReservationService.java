package org.ratifire.admin.carrentalsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ratifire.admin.carrentalsystem.converter.ReservationConverter;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.exception.ResourceNotFoundException;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.ratifire.admin.carrentalsystem.repository.ReservationRepository;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Transactional
    public ReservationDto create(ReservationDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + dto.getCarId()));

        LocalDateTime endDateTime = dto.getStartDateTime().plusDays(dto.getDays());

        boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                car.getId(), dto.getStartDateTime(), endDateTime);

        if (hasOverlap) {
            throw new IllegalStateException("Car with plate " + car.getPlateNumber()
                    + " is not available for the requested period");
        }

        var reservation = ReservationConverter.toEntity(dto, user, car);
        var saved = reservationRepository.save(reservation);
        log.info("Created reservation with id: {}", saved.getId());
        return ReservationConverter.toDto(saved);
    }

    public List<ReservationDto> getByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(ReservationConverter::toDto)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
        log.info("Deleted reservation with id: {}", id);
    }
}
