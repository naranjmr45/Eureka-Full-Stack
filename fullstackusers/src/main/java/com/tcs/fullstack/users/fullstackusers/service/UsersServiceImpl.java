package com.tcs.fullstack.users.fullstackusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcs.fullstack.users.fullstackusers.data.UserEntity;
import com.tcs.fullstack.users.fullstackusers.data.UsersRepository;
import com.tcs.fullstack.users.fullstackusers.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncode) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncode;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(
                bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();

        // Makes it so the userEntity and userDetails have the exact same fields to map
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        usersRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto updateUser(UserDto userDetails) {
        UserEntity userEntity = usersRepository.findByUserId(userDetails.getUserId());

        if (userEntity == null) {
            return null;
        }

        userEntity.setFirstName(userDetails.getFirstName());
        userEntity.setLastName(userDetails.getLastName());
        userEntity.setEmail(userDetails.getEmail());

        usersRepository.save(userEntity);

        UserDto returnValue = new ModelMapper().map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        List<UserDto> users = new ArrayList<>();
        usersRepository.findAll().forEach(userEntities::add);

        for (int i = 0; i < userEntities.size(); i++) {
            users.add(new ModelMapper().map(userEntities.get(i), UserDto.class));
        }
        return users;
    }

    @Override
    public UserDto deleteUserByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        usersRepository.delete(userEntity);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

}
