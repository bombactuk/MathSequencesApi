package com.task.mathsequences.service;


import com.task.mathsequences.dto.RegistrationUserDto;
import com.task.mathsequences.dto.request.JwtRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> createAuthToken(JwtRequestDto authRequest);

    ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto);

}
