package com.interview.ai_interview.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailService {
    UserDetails loadUserByUsername(String email);
    
}
