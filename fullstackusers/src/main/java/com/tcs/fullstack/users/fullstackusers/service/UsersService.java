package com.tcs.fullstack.users.fullstackusers.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tcs.fullstack.users.fullstackusers.shared.UserDto;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDetails);
    UserDto getUserDetailsByEmail(String email);
    UserDto updateUser(UserDto userDetails);
    UserDto deleteUserByEmail(String email);
    List<UserDto> getAllUsers();
}
