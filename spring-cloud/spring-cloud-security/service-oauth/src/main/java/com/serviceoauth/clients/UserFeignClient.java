package com.serviceoauth.clients;

import com.commonsusers.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserFeignClient {

    @GetMapping("/users/search/searchByUsername")
    User findByUsernmae(@RequestParam("username") String username);
}
