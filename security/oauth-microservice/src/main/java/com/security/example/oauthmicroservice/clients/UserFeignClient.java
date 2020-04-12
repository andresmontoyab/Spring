package com.security.example.oauthmicroservice.clients;

import com.security.example.usermicroservice.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-microservices", url = "http://localhost:8283")
public interface UserFeignClient {

    @GetMapping("/users/search/searchByUsername")
    User findByUsernmae(@RequestParam("username") String username);
}
