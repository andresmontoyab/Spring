package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;
import com.microservices.microservicesitems.domain.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Primary
public class ItemServiceImpl implements ItemService{

    private final RestTemplate restTemplate;

    public ItemServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Item> findAll() {
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://products-service/products", Product[].class));
        return products.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer amount) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id.toString());
        Product product = restTemplate.getForObject("http://products-service/products/{id}", Product.class, parameters);
        return new Item(product,amount);
    }

    @Override
    public Item findByIdtimeout() {
        Product product = restTemplate.getForObject("http://products-service/products/timeOut", Product.class);
        return new Item(product , 1);
    }
}
