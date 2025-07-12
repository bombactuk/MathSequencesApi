package com.task.mathsequences.service;



import com.task.mathsequences.dto.RegistrationUserDto;
import com.task.mathsequences.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User createNewUser(RegistrationUserDto registrationUserDto);

}
