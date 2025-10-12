package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.UserErrorConstant;
import com.example.zerowaste_api.entity.Users;
import com.example.zerowaste_api.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsersDAO {

    private final UserRepository userRepository;

    public UsersDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users save(Users user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return userRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("Username is empty");
        }
        return userRepository.findByUsername(username);
    }

    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ServiceAppException(HttpStatus.BAD_REQUEST, UserErrorConstant.USER_NOT_FOUND));
    }

    public Optional<Object> findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new RuntimeException("Email is empty");
        }
        return userRepository.findByEmail(email);
    }
}
