package com.tcs.fullstack.users.fullstackusers.data;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
