package com.schoolProject.ecommerceApplication.service.impl;

import com.schoolProject.ecommerceApplication.dto.LoginRequest;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.dto.UserDto;
import com.schoolProject.ecommerceApplication.entity.User;
import com.schoolProject.ecommerceApplication.enums.UserRole;
import com.schoolProject.ecommerceApplication.exception.InvalidCredentialException;
import com.schoolProject.ecommerceApplication.exception.NotFoundException;
import com.schoolProject.ecommerceApplication.mapper.EntityDtoMapper;
import com.schoolProject.ecommerceApplication.repository.UserRepo;
import com.schoolProject.ecommerceApplication.security.JwtUtils;
import com.schoolProject.ecommerceApplication.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
       UserRole role = UserRole.USER;

       if (registrationRequest.getRole() != null && registrationRequest.getRole().equalsIgnoreCase("admin")){
           role = UserRole.ADMIN;
       }

       //add @Builder annotation in the user class so that this can work
       User user = User.builder()
               .name(registrationRequest.getName())
               .email(registrationRequest.getEmail())
               .password(passwordEncoder.encode(registrationRequest.getPassword()))
               .phoneNumber((registrationRequest.getPhoneNumber()))
               .role(role)
               .build();

       User savedUser = userRepo.save(user);

       UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
       return Response.builder()
               .status(200)
               .message("User successfully Added")
               .user(userDto)
               .build();
    }


    @Override
    public Response loginUser(LoginRequest loginRequest) {
       User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new NotFoundException("email not found"));

       if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
           throw new InvalidCredentialException("Password does not match");
       }
       String token = jwtUtils.generateToken(user);



        return Response.builder()
                .status(200)
                .message("User successfully logged in")
                .token(token)
                .expirationTime("6 month")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .message("successful")
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String email = authentication.getName();
      log.info("User email is:" + email);

      return userRepo.findByEmail(email)
              .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
