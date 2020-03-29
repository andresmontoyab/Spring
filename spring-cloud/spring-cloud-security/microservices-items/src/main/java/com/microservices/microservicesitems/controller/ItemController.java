package com.microservices.microservicesitems.controller;

import com.microservices.microservicesitems.domain.Item;
import com.microservices.microservicesitems.service.ItemService;
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

    @GetMapping("/items")
    public List<Item> findAll(){
        return itemService.findAll();
    }

    @GetMapping("/items/{id}/amount/{amount}")
    public Item findById(@PathVariable Long id, @PathVariable Integer amount) {
        return itemService.findById(id, amount);
    }


}
