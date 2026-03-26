package org.ratifire.admin.carrentalsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.UserDto;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserDto me(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow();
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
