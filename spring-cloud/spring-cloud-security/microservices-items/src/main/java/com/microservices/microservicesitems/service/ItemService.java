package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;
import com.spring.commons.appcommons.models.entity.Product;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long id, Integer amount);
    Item findByIdtimeout();
    Product save(Product product);
    Product update(Product product, Long id);
    void delete(Long id);
}
