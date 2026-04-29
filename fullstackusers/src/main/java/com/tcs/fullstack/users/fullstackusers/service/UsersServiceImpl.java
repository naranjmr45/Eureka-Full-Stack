package com.tcs.fullstack.users.fullstackusers.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcs.fullstack.users.fullstackusers.data.AuthorityEntity;
import com.tcs.fullstack.users.fullstackusers.data.AuthorityRepository;
import com.tcs.fullstack.users.fullstackusers.data.ItemsServiceClient;
import com.tcs.fullstack.users.fullstackusers.data.RoleEntity;
import com.tcs.fullstack.users.fullstackusers.data.RoleRepository;
import com.tcs.fullstack.users.fullstackusers.data.UserEntity;
import com.tcs.fullstack.users.fullstackusers.data.UsersRepository;
import com.tcs.fullstack.users.fullstackusers.shared.Roles;
import com.tcs.fullstack.users.fullstackusers.shared.UserDto;
import com.tcs.fullstack.users.fullstackusers.ui.model.ItemResponseModel;

import feign.FeignException;
import jakarta.transaction.Transactional;

/**
 * A class that implements the UsersService interface and handles the
 * implementations of the database operations for the users microservice.
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    UsersRepository usersRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    ItemsServiceClient itemsServiceClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * A constructor that gets the usersRepository, bCryptPasswordEncoder, and
     * itemsServiceClient via constructor dependecy injection.
     * 
     * @param usersRepository      The jpa repository for the users microservice.
     * @param bCryptPasswordEncode An encryption component for the user passwords.
     * @param itemsServiceClient   A component used to reference the items
     *                             microservice.
     */
    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncode,
            ItemsServiceClient itemsServiceClient) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncode;
        this.itemsServiceClient = itemsServiceClient;
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

        AuthorityEntity readAuthority = createAuthority("READ");
        AuthorityEntity writeAuthority = createAuthority("WRITE");

        RoleEntity roleUser = createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));

        userEntity.setRoles(Arrays.asList(roleUser));

        UserEntity storedUserDetails = usersRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto createAdmin(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(
                bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();

        // Makes it so the userEntity and userDetails have the exact same fields to map
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        AuthorityEntity readAuthority = createAuthority("READ");
        AuthorityEntity writeAuthority = createAuthority("WRITE");
        AuthorityEntity deleteAuthority = createAuthority("DELETE");

        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(),
                Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        userEntity.setRoles(Arrays.asList(roleAdmin));

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

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Collection<RoleEntity> roles = userEntity.getRoles();

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            Collection<AuthorityEntity> authorityEntities = role.getAuthorities();
            authorityEntities.forEach((authorityEntity) -> {
                authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
            });
        });

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                authorities);
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
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // String itemsUrl = String.format("http://ITEMS-WS/users/%s/items", userId);

        // ResponseEntity<List<ItemResponseModel>> itemsListResponse =
        // restTemplate.exchange(itemsUrl, HttpMethod.GET, null, new
        // ParameterizedTypeReference<List<ItemResponseModel>>() {});
        // List<ItemResponseModel> itemList = itemsListResponse.getBody();

        List<ItemResponseModel> itemList = itemsServiceClient.getItems(userId);

        userDto.setItems(itemList);

        return userDto;
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<UserEntity> userEntities = usersRepository.findAll(pageable);

        ModelMapper modelMapper = new ModelMapper();

        return userEntities.map(entity -> modelMapper.map(entity, UserDto.class));
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

    @Transactional
    private AuthorityEntity createAuthority(String name) {

        AuthorityEntity authority = authorityRepository.findByName(name);

        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }

        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {

        RoleEntity role = roleRepository.findByName(name);

        if (role == null) {
            role = new RoleEntity(name, authorities);
            roleRepository.save(role);
        }

        return role;
    }

}
