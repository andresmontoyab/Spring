package com.microservices.microservicesproducts.controller;

import com.microservices.microservicesproducts.domain.entities.Product;
import com.microservices.microservicesproducts.service.IProductService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private final IProductService productService;
    private final Environment environment;

    public ProductController(IProductService productService, Environment environment) {
        this.productService = productService;
        this.environment = environment;
    }

    @GetMapping("/products")
    public List<Product> findAll(){
        return productService.findAll().stream()
                .map(product ->
                {
                    product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    return product;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable Long id){
        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return productService.findById(id);
    }
}
