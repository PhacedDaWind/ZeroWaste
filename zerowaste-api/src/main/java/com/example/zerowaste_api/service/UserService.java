package com.example.zerowaste_api.service;

import com.example.zerowaste_api.converter.UsersConverter;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.UserDetailsResDTO;
import com.example.zerowaste_api.dto.UserDetailsTuple;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersDAO usersDAO;

    private final UsersConverter usersConverter;

    public UserService(UsersDAO usersDAO, UsersConverter usersConverter) {
        this.usersDAO = usersDAO;
        this.usersConverter = usersConverter;
    }

    public UserDetailsResDTO read(@Min(1) Long id) {
        UserDetailsTuple tuple = usersDAO.findUserDetailsTuple(id);
        return usersConverter.toUserDetailsResDTO(tuple);
    }
}
