package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.converter.UsersConverter;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.UserRegistrationReqDTO;
import com.example.zerowaste_api.dto.UserRegistrationResDTO;
import com.example.zerowaste_api.entity.Users;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UsersDAO usersDAO;
    private final UsersConverter usersConverter;

    public RegistrationService(UsersDAO usersDAO, UsersConverter usersConverter) {
        this.usersDAO = usersDAO;
        this.usersConverter = usersConverter;
    }

    public UserRegistrationResDTO registerUser(UserRegistrationReqDTO request) {
        // Check if a user with the given username already exists
        if (usersDAO.findByUsername(request.getUsername()).isPresent()) {
      throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Error: Username is already taken!");
        }
        if (usersDAO.findByEmail(request.getEmail()).isPresent()) {
      throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Error: Email is already taken!");
        }

        Users newUser = new Users();
        newUser = usersConverter.toUser(newUser, request);

        usersDAO.save(newUser);
        return usersConverter.toUserRegistrationResDTO(newUser);
    }
}
