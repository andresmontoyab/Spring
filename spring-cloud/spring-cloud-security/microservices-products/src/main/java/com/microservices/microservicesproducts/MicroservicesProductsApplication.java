package com.microservices.microservicesproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroservicesProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesProductsApplication.class, args);
	}

}
