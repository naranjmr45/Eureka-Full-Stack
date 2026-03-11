package com.tcs.fullstack.users.fullstackusers.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.fullstack.users.fullstackusers.service.UsersService;
import com.tcs.fullstack.users.fullstackusers.shared.UserDto;
import com.tcs.fullstack.users.fullstackusers.ui.model.CreateUserRequestModel;
import com.tcs.fullstack.users.fullstackusers.ui.model.CreateUserResponseModel;
import com.tcs.fullstack.users.fullstackusers.ui.model.UpdateUserRequestModel;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;

    @GetMapping("/status/check")
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK).body("Working on port " + env.getProperty("local.server.port"));
    }

    @GetMapping(params = "email")
    public ResponseEntity<CreateUserResponseModel> findByEmail(@RequestParam String email) {

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.getUserDetailsByEmail(email);
        CreateUserResponseModel returnValue = modelMapper.map(userDetails, CreateUserResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or principal == #userId")
    //@PreAuthorize("principal == #userId")
    //@PostAuthorize("principal == returnObject.body.userId()")
    public ResponseEntity<CreateUserResponseModel> findByUserId(@PathVariable String userId) {

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.getUserDetailsByUserId(userId);
        CreateUserResponseModel returnValue = modelMapper.map(userDetails, CreateUserResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping()
    public ResponseEntity<List<CreateUserResponseModel>> getAllUsers() {

        ModelMapper modelMapper = new ModelMapper();

        List<UserDto> userList = usersService.getAllUsers();
        List<CreateUserResponseModel> returnValue = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            returnValue.add(modelMapper.map(userList.get(i), CreateUserResponseModel.class));
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

        ModelMapper modelMapper = new ModelMapper();

        // Makes it so the userEntity and userDetails have the exact same fields to map
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = usersService.createUser(userDto);

        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<CreateUserResponseModel> createAdmin(@Valid @RequestBody CreateUserRequestModel userDetails) {
        
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdAdmin = usersService.createAdmin(userDto);
        
        CreateUserResponseModel returnValue = modelMapper.map(createdAdmin, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #userDetails.getUserId()")
    @PatchMapping()
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto updatedUser = usersService.updateUser(userDto);

        if (updatedUser != null) {
            CreateUserResponseModel returnValue = modelMapper.map(updatedUser, CreateUserResponseModel.class);

            return ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserId was not found");
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROFILE_DELETE') or principal == #userId")
    @DeleteMapping(params = "email")
    public ResponseEntity<CreateUserResponseModel> deleteUser(@RequestParam String email) {
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.deleteUserByEmail(email);
        CreateUserResponseModel returnValue = modelMapper.map(userDetails, CreateUserResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
