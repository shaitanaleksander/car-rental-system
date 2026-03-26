package org.ratifire.admin.carrentalsystem.converter;

import org.junit.jupiter.api.Test;
import org.ratifire.admin.carrentalsystem.dto.UserDto;
import org.ratifire.admin.carrentalsystem.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    @Test
    void toDto_shouldMapAllFields() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("secret")
                .build();

        UserDto dto = UserConverter.toDto(user);

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
    }

    @Test
    void toEntity_shouldMapFieldsWithoutId() {
        UserDto dto = UserDto.builder()
                .id(5L)
                .name("Jane Smith")
                .email("jane@example.com")
                .build();

        User user = UserConverter.toEntity(dto);

        assertNull(user.getId());
        assertEquals("Jane Smith", user.getName());
        assertEquals("jane@example.com", user.getEmail());
    }
}
