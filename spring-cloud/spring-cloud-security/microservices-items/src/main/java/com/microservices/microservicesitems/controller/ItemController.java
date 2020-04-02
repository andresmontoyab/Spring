package com.microservices.microservicesitems.controller;

import com.microservices.microservicesitems.domain.Item;
import com.microservices.microservicesitems.domain.Product;
import com.microservices.microservicesitems.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
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


}
