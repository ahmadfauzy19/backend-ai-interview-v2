package com.interview.ai_interview.controllers;

import com.interview.ai_interview.dto.request.LoginRequest;
import com.interview.ai_interview.dto.request.RegisterRequest;
import com.interview.ai_interview.dto.response.AuthResponse;
import com.interview.ai_interview.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Karena JWT stateless, logout hanya di client side (hapus token)
        return ResponseEntity.ok("Logout success (remove token on client)");
    }
}