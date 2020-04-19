package com.microservices.microservicesitems.feign;

import com.spring.commons.appcommons.models.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products-service")
public interface FeignClientProducts {

    @GetMapping("/")
    List<Product> findAll();

    @GetMapping("/{id}")
    Product findById(@PathVariable("id") Long id);

    @PostMapping("/create")
    Product createProduct(@RequestBody Product product);

    @PutMapping("/update/{id}")
    Product updateProduct(@RequestBody Product product, @PathVariable("id") Long id);

    @DeleteMapping("/delete/{id}")
    Product deleteProduct(@PathVariable("id") Long id);



}
