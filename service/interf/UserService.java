package com.schoolProject.ecommerceApplication.service.interf;

import com.schoolProject.ecommerceApplication.dto.LoginRequest;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.dto.UserDto;
import com.schoolProject.ecommerceApplication.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getLoginUser();

    Response getUserInfoAndOrderHistory();
}
