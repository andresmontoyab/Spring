package com.microservices.microservicesproducts.controller;

import com.spring.commons.appcommons.models.entity.Product;
import com.microservices.microservicesproducts.service.IProductService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
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

    @GetMapping("/")
    public @ResponseBody List<Product> findAll(){
        return productService.findAll().stream()
                .map(product ->
                {
                    product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    return product;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id){
        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return productService.findById(id);
    }

    @GetMapping("/timeOut")
    public  Product findAllTimeOut() throws InterruptedException {
        Thread.sleep(10000L);
        return null;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product updateProduct(@RequestBody Product product, @PathVariable Long id) {
        Product productServiceById = productService.findById(id);
        productServiceById.setName(product.getName());
        productServiceById.setPrice(product.getPrice());
        return productService.save(productServiceById);
    }


}
