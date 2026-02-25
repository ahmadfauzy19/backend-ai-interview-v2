package com.interview.ai_interview.dto.request;

import com.interview.ai_interview.models.UserRole;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
}