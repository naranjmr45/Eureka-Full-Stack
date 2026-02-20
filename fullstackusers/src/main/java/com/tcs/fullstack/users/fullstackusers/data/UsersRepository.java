package com.tcs.fullstack.users.fullstackusers.data;

import org.springframework.data.repository.CrudRepository;


public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
