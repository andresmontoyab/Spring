package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;
import com.spring.commons.appcommons.models.entity.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://products-service/", Product[].class));
        return products.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer amount) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id.toString());
        Product product = restTemplate.getForObject("http://products-service/{id}", Product.class, parameters);
        return new Item(product,amount);
    }

    @Override
    public Item findByIdtimeout() {
        Product product = restTemplate.getForObject("http://products-service/timeOut", Product.class);
        return new Item(product , 1);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<>(product);
        ResponseEntity<Product> response = restTemplate.exchange("http://products-service/create", HttpMethod.POST, body, Product.class);
        return response.getBody();
    }

    @Override
    public Product update(Product product, Long id) {
        HttpEntity<Product> body = new HttpEntity<>(product);
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        ResponseEntity<Product> response = restTemplate.exchange(
                "http://products-service/update/{id}",
                HttpMethod.PUT,
                body,
                Product.class,
                pathVariables);
        return response.getBody();
    }

    @Override
    public void delete(Long id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        restTemplate.delete("http://products-service/delete/{id}", pathVariables);
    }
}
