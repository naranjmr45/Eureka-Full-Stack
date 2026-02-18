package com.tcs.fullstack.discovery.fullstackdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FullstackdiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackdiscoveryApplication.class, args);
	}

}
