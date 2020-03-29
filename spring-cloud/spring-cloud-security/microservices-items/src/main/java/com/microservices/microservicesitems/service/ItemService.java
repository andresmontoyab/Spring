package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long id, Integer amount);
}
