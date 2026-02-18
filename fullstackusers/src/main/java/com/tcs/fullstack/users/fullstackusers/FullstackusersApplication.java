package com.tcs.fullstack.users.fullstackusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FullstackusersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackusersApplication.class, args);
	}

}
