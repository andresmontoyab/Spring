package guru.springframework.didemo.controllers;

import guru.springframework.didemo.services.GreetingService;
import guru.springframework.didemo.services.GreetingServiceFactory;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

    private GreetingService  greetingService;

    public MyController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String hello()
    {
       return greetingService.sayGreetings();
    }
}
