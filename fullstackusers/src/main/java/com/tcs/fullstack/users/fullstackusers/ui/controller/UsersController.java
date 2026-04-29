package com.tcs.fullstack.users.fullstackusers.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.tcs.fullstack.users.fullstackusers.ui.model.UserResponseModel;

import jakarta.validation.Valid;

/**
 * This class contains the Requests that can be send to the users microservice.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;

    /**
     * This request checks the status of the user microservice as well as the port
     * it is on.
     * 
     * @return The port number of the microservice with 200 OK
     */
    @GetMapping("/status/check")
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK).body("Working on port " + env.getProperty("local.server.port"));
    }

    /**
     * This request searches for the user in the database via email.
     * 
     * @param email The email id found in the query string on the url.
     * @return The first name, last name, userId, and email of the user if found in
     *         the database in the body of the response.
     */
    @GetMapping(params = "email")
    public ResponseEntity<CreateUserResponseModel> findByEmail(@RequestParam String email) {

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.getUserDetailsByEmail(email);
        CreateUserResponseModel returnValue = modelMapper.map(userDetails, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    /**
     * This request is to find a user by their userId. A user can only serach for
     * their own account via userId. A user with admin privliages can search for any
     * account via userId.
     * 
     * @param userId The userId found in the path of the request.
     * @return The first name, last name, userId, and email of the user if found in
     *         the database in the body of the response.
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or principal == #userId")
    // @PreAuthorize("principal == #userId")
    // @PostAuthorize("principal == returnObject.body.userId()")
    public ResponseEntity<UserResponseModel> findByUserId(@PathVariable String userId) {

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.getUserDetailsByUserId(userId);
        UserResponseModel returnValue = modelMapper.map(userDetails, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    /**
     * This requst is to get all users in the database. By default, only 5 users
     * will be displayed at a time and the client will need to move through the
     * different pages to see different users.
     * 
     * @param pageable An object in the request url that will have values for the
     *                 size and page of the pageable list. By default, the size will
     *                 be 5 and the page will be 0.
     * @return A pageable list of all the users in the database in the response
     *         body.
     */
    @GetMapping()
    public ResponseEntity<Page<CreateUserResponseModel>> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {

        ModelMapper modelMapper = new ModelMapper();

        Page<UserDto> userPage = usersService.getAllUsers(pageable);

        Page<CreateUserResponseModel> returnValue = userPage
                .map(dto -> modelMapper.map(dto, CreateUserResponseModel.class));

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    /**
     * This request will create a new user in the database if the user doesn't
     * already exist.
     * 
     * @param userDetails The client will provide a valid first name, last name,
     *                    password, and email as part of the request.
     * @return The first name, last name, userId, and email of the user if the user
     *         has been successfuly create in
     *         the database as part of the body of the response.
     */
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

    /**
     * This request is to create a new user with admin privliages. This request can
     * only be done by an existing admin.
     * 
     * @param userDetails The client will provide a valid first name, last name,
     *                    password, and email as part of the request.
     * @return The first name, last name, userId, and email of the user if the user
     *         has been successfuly create in
     *         the database with admin privliages as part of the body of the
     *         response.
     */
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

    /**
     * This request is to update an existing user in the database. This can only be
     * performed by a user with admin privaleges or a user on their own account.
     * 
     * @param userDetails The client will provide a valid first name, last name,
     *                    and/or email as part of the request.
     * @return The first name, last name, userId, and email of the user if the user
     *         has been successfuly updated in
     *         the database as part of the body of the response.
     */
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

    /**
     * This request is to delete an existing user in the database. This can only be
     * performed by a user with admin privaleges or a user on their own account.
     * 
     * @param email The email id found in the query string on the url.
     * @return The first name, last name, userId, and email of the user if found and
     *         deleted from the database in the body of the response.
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROFILE_DELETE') or principal == #userId")
    @DeleteMapping(params = "email")
    public ResponseEntity<CreateUserResponseModel> deleteUser(@RequestParam String email) {
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDetails = usersService.deleteUserByEmail(email);
        CreateUserResponseModel returnValue = modelMapper.map(userDetails, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
