package org.ratifire.admin.carrentalsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.exception.ResourceNotFoundException;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.ratifire.admin.carrentalsystem.repository.ReservationRepository;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ReservationService reservationService;

    private final LocalDateTime startDateTime = LocalDateTime.of(2026, 4, 1, 9, 0);

    private User buildUser() {
        return User.builder().id(1L).name("John").email("john@example.com").password("pass").build();
    }

    private Car buildCar() {
        return Car.builder().id(2L).carType(CarType.SEDAN).plateNumber("AB-123-CD").build();
    }

    private Reservation buildReservation(User user, Car car) {
        return Reservation.builder()
                .id(10L)
                .user(user)
                .car(car)
                .startDateTime(startDateTime)
                .days(3)
                .build();
    }

    private ReservationDto buildDto() {
        return ReservationDto.builder()
                .userId(1L)
                .carId(2L)
                .startDateTime(startDateTime)
                .days(3)
                .build();
    }

    // --- create ---

    @Test
    void create_shouldSaveAndReturnDto() {
        User user = buildUser();
        Car car = buildCar();
        ReservationDto dto = buildDto();
        Reservation saved = buildReservation(user, car);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));
        when(reservationRepository.existsOverlappingReservation(eq(2L), eq(startDateTime), any())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);

        ReservationDto result = reservationService.create(dto);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getCarId());
        assertEquals(startDateTime, result.getStartDateTime());
        assertEquals(3, result.getDays());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void create_shouldThrow_whenUserNotFound() {
        ReservationDto dto = buildDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.create(dto));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_shouldThrow_whenCarNotFound() {
        ReservationDto dto = buildDto();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buildUser()));
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.create(dto));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_shouldThrow_whenCarNotAvailable() {
        ReservationDto dto = buildDto();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buildUser()));
        when(carRepository.findById(2L)).thenReturn(Optional.of(buildCar()));
        when(reservationRepository.existsOverlappingReservation(eq(2L), eq(startDateTime), any())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> reservationService.create(dto));
        verify(reservationRepository, never()).save(any());
    }

    // --- getById ---

    @Test
    void getById_shouldReturnDto_whenFound() {
        Reservation reservation = buildReservation(buildUser(), buildCar());
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));

        ReservationDto result = reservationService.getById(10L);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getCarId());
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getById(99L));
    }

    // --- getByUserId ---

    @Test
    void getByUserId_shouldReturnList() {
        User user = buildUser();
        Car car = buildCar();
        List<Reservation> reservations = List.of(
                buildReservation(user, car),
                Reservation.builder().id(11L).user(user).car(car)
                        .startDateTime(startDateTime.plusDays(10)).days(2).build()
        );

        when(reservationRepository.findByUserId(1L)).thenReturn(reservations);

        List<ReservationDto> result = reservationService.getByUserId(1L);

        assertEquals(2, result.size());
    }

    @Test
    void getByUserId_shouldReturnEmptyList_whenNoneFound() {
        when(reservationRepository.findByUserId(99L)).thenReturn(List.of());

        List<ReservationDto> result = reservationService.getByUserId(99L);

        assertTrue(result.isEmpty());
    }

    // --- update ---

    @Test
    void update_shouldUpdateAndReturnDto() {
        User user = buildUser();
        Car car = buildCar();
        Reservation existing = buildReservation(user, car);
        LocalDateTime newStart = startDateTime.plusDays(5);

        ReservationDto dto = ReservationDto.builder()
                .carId(2L)
                .startDateTime(newStart)
                .days(4)
                .build();

        Reservation updated = Reservation.builder()
                .id(10L).user(user).car(car).startDateTime(newStart).days(4).build();

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));
        when(reservationRepository.existsOverlappingReservation(eq(2L), eq(newStart), any())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updated);

        ReservationDto result = reservationService.update(10L, dto);

        assertEquals(10L, result.getId());
        assertEquals(newStart, result.getStartDateTime());
        assertEquals(4, result.getDays());
    }

    @Test
    void update_shouldThrow_whenReservationNotFound() {
        ReservationDto dto = buildDto();
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.update(99L, dto));
    }

    @Test
    void update_shouldThrow_whenCarNotFound() {
        Reservation existing = buildReservation(buildUser(), buildCar());
        ReservationDto dto = buildDto();

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.update(10L, dto));
    }

    @Test
    void update_shouldThrow_whenNewCarNotAvailable() {
        User user = buildUser();
        Car oldCar = Car.builder().id(3L).carType(CarType.SUV).plateNumber("XX-999-YY").build();
        Car newCar = buildCar();
        Reservation existing = Reservation.builder()
                .id(10L).user(user).car(oldCar).startDateTime(startDateTime).days(3).build();

        ReservationDto dto = buildDto();

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(carRepository.findById(2L)).thenReturn(Optional.of(newCar));
        when(reservationRepository.existsOverlappingReservation(eq(2L), eq(startDateTime), any())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> reservationService.update(10L, dto));
        verify(reservationRepository, never()).save(any());
    }

    // --- delete ---

    @Test
    void delete_shouldDelete_whenExists() {
        when(reservationRepository.existsById(10L)).thenReturn(true);

        reservationService.delete(10L);

        verify(reservationRepository).deleteById(10L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(reservationRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.delete(99L));
        verify(reservationRepository, never()).deleteById(any());
    }
}
