package com.jpa.hibernate.springhibernatedepth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
public class Controller {

    @GetMapping
    public String getString() {
        return "This is the controller";
    }
}
