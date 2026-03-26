package org.ratifire.admin.carrentalsystem.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.ratifire.admin.carrentalsystem.repository.ReservationRepository;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";

    private User testUser;
    private Car testCar;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(User.builder()
                .name("Test User")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build());

        testCar = carRepository.save(Car.builder()
                .carType(CarType.SEDAN)
                .plateNumber("AB-123-CD")
                .build());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        ReservationDto dto = ReservationDto.builder()
                .userId(testUser.getId())
                .carId(testCar.getId())
                .startDateTime(LocalDateTime.of(2026, 5, 1, 9, 0))
                .days(3)
                .build();

        mockMvc.perform(post("/api/reservations")
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.carId", is(testCar.getId().intValue())))
                .andExpect(jsonPath("$.days", is(3)));
    }

    @Test
    void create_shouldReturn409_whenOverlap() throws Exception {
        reservationRepository.save(Reservation.builder()
                .user(testUser)
                .car(testCar)
                .startDateTime(LocalDateTime.of(2026, 5, 1, 9, 0))
                .days(5)
                .build());

        ReservationDto dto = ReservationDto.builder()
                .userId(testUser.getId())
                .carId(testCar.getId())
                .startDateTime(LocalDateTime.of(2026, 5, 3, 10, 0))
                .days(2)
                .build();

        mockMvc.perform(post("/api/reservations")
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void getByUserId_shouldReturnReservations() throws Exception {
        reservationRepository.save(Reservation.builder()
                .user(testUser)
                .car(testCar)
                .startDateTime(LocalDateTime.of(2026, 5, 1, 9, 0))
                .days(3)
                .build());

        mockMvc.perform(get("/api/reservations")
                        .param("userId", testUser.getId().toString())
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].carId", is(testCar.getId().intValue())));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .user(testUser)
                .car(testCar)
                .startDateTime(LocalDateTime.of(2026, 5, 1, 9, 0))
                .days(3)
                .build());

        mockMvc.perform(delete("/api/reservations/{id}", reservation.getId())
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/api/reservations/{id}", 9999)
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isNotFound());
    }
}
