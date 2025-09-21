package com.example.zerowaste_api.converter;

import com.example.zerowaste_api.dto.UserRegistrationReqDTO;
import com.example.zerowaste_api.dto.UserRegistrationResDTO;
import com.example.zerowaste_api.dto.UserResponseDTO;
import com.example.zerowaste_api.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsersConverter {

    private final PasswordEncoder passwordEncoder;

    public UsersConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Users toUser(Users user, UserRegistrationReqDTO registrationReqDTO) {
        user.setUsername(registrationReqDTO.getUsername());
        user.setEmail(registrationReqDTO.getEmail());
        user.setStatus(registrationReqDTO.getStatus());
        user.setHouseholdSize(registrationReqDTO.getHouseholdSize());
        user.setTwoFactorAuthEnabled(registrationReqDTO.getTwoFactorAuthEnabled());
        user.setPassword(passwordEncoder.encode(registrationReqDTO.getPassword()));
        return user;
    }

    public UserRegistrationResDTO toUserRegistrationResDTO(Users user) {
        UserRegistrationResDTO response = new UserRegistrationResDTO();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setHouseholdSize(user.getHouseholdSize());
        response.setTwoFactorAuthEnabled(user.getTwoFactorAuthEnabled());
        response.setStatus(user.getStatus());
        return response;
    }

    public UserResponseDTO toUserResponseDTO(Users user) {
        if (Objects.isNull(user)) {
            return null;
        }
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setHouseholdSize(user.getHouseholdSize());
        response.setTwoFactorAuthEnabled(user.getTwoFactorAuthEnabled());
        response.setStatus(user.getStatus());
        return response;
    }
}
