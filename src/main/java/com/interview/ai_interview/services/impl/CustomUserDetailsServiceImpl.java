package com.interview.ai_interview.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.interview.ai_interview.services.CustomUserDetailService;
import com.interview.ai_interview.utils.CustomUserDetail;
import com.interview.ai_interview.models.User;
import com.interview.ai_interview.repositories.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailService {
    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetail(user);
    }

    
}
