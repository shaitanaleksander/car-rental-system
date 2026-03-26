package org.ratifire.admin.carrentalsystem.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.UserDto;
import org.ratifire.admin.carrentalsystem.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
