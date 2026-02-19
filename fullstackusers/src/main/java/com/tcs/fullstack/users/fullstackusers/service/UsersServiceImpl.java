package com.tcs.fullstack.users.fullstackusers.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.fullstack.users.fullstackusers.data.UserEntity;
import com.tcs.fullstack.users.fullstackusers.data.UsersRepository;
import com.tcs.fullstack.users.fullstackusers.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();

        // Makes it so the userEntity and userDetails have the exact same fields to map
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword("test");

        usersRepository.save(userEntity);

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
