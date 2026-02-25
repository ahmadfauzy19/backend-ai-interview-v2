package com.interview.ai_interview.services;

import com.interview.ai_interview.models.User;
import com.interview.ai_interview.repositories.UserRepository;
import com.interview.ai_interview.security.JwtSecurity;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.interview.ai_interview.dto.response.AuthResponse;
import com.interview.ai_interview.dto.request.LoginRequest;
import com.interview.ai_interview.dto.request.RegisterRequest;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurity jwtSecurity;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // System.out.println(new BCryptPasswordEncoder().encode("admin123"));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtSecurity.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .build();
    }
}