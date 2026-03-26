package org.ratifire.admin.carrentalsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ratifire.admin.carrentalsystem.entity.Car;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.repository.CarRepository;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .name("Test User")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build());

        carRepository.save(Car.builder().carType(CarType.SEDAN).plateNumber("AB-001").build());
        carRepository.save(Car.builder().carType(CarType.SEDAN).plateNumber("AB-002").build());
        carRepository.save(Car.builder().carType(CarType.SUV).plateNumber("CD-001").build());
        carRepository.save(Car.builder().carType(CarType.VAN).plateNumber("EF-001").build());
    }

    @Test
    void getCars_shouldReturnAllCars() throws Exception {
        mockMvc.perform(get("/api/cars")
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void getCars_shouldFilterByType() throws Exception {
        mockMvc.perform(get("/api/cars")
                        .param("type", "SEDAN")
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].carType", is("SEDAN")));
    }

    @Test
    void getCars_shouldReturnEmpty_whenNoMatchingType() throws Exception {
        mockMvc.perform(get("/api/cars")
                        .param("type", "VAN")
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnCar() throws Exception {
        Car car = carRepository.save(Car.builder().carType(CarType.SUV).plateNumber("ZZ-999").build());

        mockMvc.perform(get("/api/cars/{id}", car.getId())
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("ZZ-999")))
                .andExpect(jsonPath("$.carType", is("SUV")));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/cars/{id}", 9999)
                        .with(httpBasic(TEST_EMAIL, TEST_PASSWORD)))
                .andExpect(status().isNotFound());
    }
}
