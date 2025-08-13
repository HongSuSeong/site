package com.water.site.service;

import com.water.site.entity.UserEntity;
import com.water.site.repository.UserRepository;
import com.water.site.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserEntity register(UserRegisterRequest request) {
        if (!userRepository.findByUsername(request.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getName())
                .role("ROLE_USER")
                .provider("local")
                .enabled(true)  // 인증 없이 바로 활성화 한다면 true
                .build();
        return userRepository.save(user);
    }
    public boolean idCheck(String username) {
        boolean idCheck = userRepository.findByUsername(username).isEmpty();
        return idCheck;
    }

    public UserEntity login (String username, String password) {
        return userRepository.findByUsername(username)
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElse(null);

    }
}