package com.task.mathsequences.service.impl;


import com.task.mathsequences.dto.RegistrationUserDto;
import com.task.mathsequences.dto.UserDto;
import com.task.mathsequences.dto.request.JwtRequestDto;
import com.task.mathsequences.dto.response.JwtResponseDto;
import com.task.mathsequences.entity.User;
import com.task.mathsequences.exception.AuthException;
import com.task.mathsequences.service.AuthService;
import com.task.mathsequences.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequestDto authRequest) {
        log.info("Attempting to authenticate user: {}", authRequest.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            log.info("User '{}' authenticated successfully", authRequest.getUsername());
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user '{}': incorrect credentials", authRequest.getUsername());
            throw new AuthException("Incorrect login or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        log.debug("Generated JWT token for user '{}'", authRequest.getUsername());

        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        log.info("Registering new user: {}", registrationUserDto.getUsername());

        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            log.warn("Password mismatch for user '{}'", registrationUserDto.getUsername());
            throw new AuthException("Passwords do not match");
        }

        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            log.warn("User '{}' already exists", registrationUserDto.getUsername());
            throw new AuthException("The user already exists");
        }

        User user = userService.createNewUser(registrationUserDto);
        log.info("User '{}' registered successfully", registrationUserDto.getUsername());

        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }

}