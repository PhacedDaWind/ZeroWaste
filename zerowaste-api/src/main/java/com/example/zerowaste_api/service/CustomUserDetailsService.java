package com.example.zerowaste_api.service;

import com.example.zerowaste_api.entity.Users;
import com.example.zerowaste_api.enums.Status;
import com.example.zerowaste_api.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch your user entity from the database
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Map your application's status to Spring Security's 'enabled' flag
        boolean isEnabled = user.getStatus() == Status.ACTIVE;

        // 3. Use the more detailed constructor to build the UserDetails object
        return new User(
                user.getUsername(),
                user.getPassword(),
                isEnabled,             // enabled
                true,                  // accountNonExpired
                true,                  // credentialsNonExpired
                true,                  // accountNonLocked
                new ArrayList<>()      // authorities (roles)
        );
    }
}