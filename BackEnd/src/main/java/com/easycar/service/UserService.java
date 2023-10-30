package com.easycar.service;

import com.easycar.dto.UserDTO;

import java.util.ArrayList;

public interface UserService {
    ArrayList<UserDTO> getAllRegUsers();

    UserDTO getRegUsers(String username,String password);

}
