package com.microservices.microservicesitems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients()
@EnableEurekaClient
@RibbonClient(name="products-service")
public class MicroservicesItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesItemsApplication.class, args);
	}

}
