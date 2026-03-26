package org.ratifire.admin.carrentalsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ratifire.admin.carrentalsystem.converter.UserConverter;
import org.ratifire.admin.carrentalsystem.dto.UserDto;
import org.ratifire.admin.carrentalsystem.entity.User;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto create(UserDto dto) {
        User user = UserConverter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User saved = userRepository.save(user);
        log.info("Created user with id: {}", saved.getId());
        return UserConverter.toDto(saved);
    }

}
