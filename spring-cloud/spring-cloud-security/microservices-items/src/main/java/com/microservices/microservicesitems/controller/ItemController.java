package com.microservices.microservicesitems.controller;

import com.microservices.microservicesitems.domain.Item;
import com.microservices.microservicesitems.domain.Product;
import com.microservices.microservicesitems.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RefreshScope // Actualiza el contexto por actuator
@RestController
public class ItemController {

    @Value("${configuration.text}")
    private String text;

    @Value("${author.name}")
    private String authorName;

    @Value("${author.lastname}")
    private String authorLastname;

    private final ItemService itemService;
    private final Environment environment;

    public ItemController(ItemService itemService, Environment environment) {
        this.itemService = itemService;
        this.environment = environment;
    }

    @GetMapping("/")
    public List<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}/amount/{amount}")
    public Item findById(@PathVariable Long id, @PathVariable Integer amount) {
        return itemService.findById(id, amount);
    }

    @GetMapping("/systemFails")
    @HystrixCommand(fallbackMethod = "systemFailsMethod")
    public Item findByIdSystemFails() throws Exception {
        throw new Exception("The application Fails");
    }

    @GetMapping("/timeOut")
    @HystrixCommand(fallbackMethod = "timeOutMethod")
    public Item findByIdTimeout() throws Exception {
        Thread.sleep(10000);
        return null;
        //return itemService.findByIdtimeout();
    }

    public Item timeOutMethod() {
        Item item = new Item();
        item.setAmount(1);
        Product product = new Product();
        product.setId(100L);
        product.setName("Default product - Time out");
        item.setProduct(product);
        return item;
    }

    public Item systemFailsMethod() {
        Item item = new Item();
        item.setAmount(1);
        Product product = new Product();
        product.setId(100L);
        product.setName("Default product - System Fails");
        item.setProduct(product);
        return item;
    }

    @GetMapping("/getConfig")
    public ResponseEntity<?> getConfig(@Value("${server.port}") String port){
        Map<String,String> jsonValue = new HashMap<>();
        jsonValue.put("text", text);
        jsonValue.put("port", port);
        jsonValue.put("name", authorName);
        jsonValue.put("lastname", authorLastname);
        Arrays.asList(environment.getActiveProfiles()).stream()
                .filter(env -> env.equalsIgnoreCase("dev"))
                .forEach(x -> jsonValue.put("environment", "We are in dev"));
        return new ResponseEntity<Map<String, String>>(jsonValue, HttpStatus.OK);
    }


}
