package org.ratifire.admin.carrentalsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ratifire.admin.carrentalsystem.dto.UserDto;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void create_shouldSaveAndReturnDto() {
        UserDto dto = UserDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("rawPassword")
                .build();

        User saved = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDto result = userService.create(dto);

        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }
}
