package com.security.example.oauthmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OauthMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OauthMicroserviceApplication.class, args);
	}

}
