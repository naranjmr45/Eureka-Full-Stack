package com.tcs.fullstack.users.fullstackusers.data;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

    RoleEntity findByName(String name);

}
