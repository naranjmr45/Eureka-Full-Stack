package com.tcs.fullstack.users.fullstackusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class FullstackusersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackusersApplication.class, args);
	}
	
	@Bean // Creates object on startup keeps it in the application context until it is needed later
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}


