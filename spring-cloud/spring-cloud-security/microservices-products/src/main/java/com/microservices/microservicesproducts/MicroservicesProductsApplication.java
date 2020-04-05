package com.microservices.microservicesproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EntityScan({"com.spring.commons.appcommons.models.entity"})
public class MicroservicesProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesProductsApplication.class, args);
	}

}
