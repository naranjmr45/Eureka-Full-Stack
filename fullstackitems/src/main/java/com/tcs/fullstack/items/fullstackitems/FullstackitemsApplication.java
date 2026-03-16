package com.tcs.fullstack.items.fullstackitems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FullstackitemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackitemsApplication.class, args);
	}

}
