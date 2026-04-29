package com.tcs.fullstack.users.fullstackusers.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.tcs.fullstack.users.fullstackusers.shared.UserDto;

/**
 * An interface that is accessable from the UserController class to perform the
 * database operations for the users microservice.
 */
public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDetails);

    UserDto getUserDetailsByEmail(String email);

    UserDto updateUser(UserDto userDetails);

    UserDto deleteUserByEmail(String email);

    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto createAdmin(UserDto userDetails);

    UserDto getUserDetailsByUserId(String userId);
}
