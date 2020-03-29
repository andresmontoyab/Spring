package com.microservices.microservicesitems.feign;

import com.microservices.microservicesitems.domain.Product;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "products-service")
@RibbonClient(name = "products-service")
public interface FeignClientProducts {

    @GetMapping("/products")
    List<Product> findAll();

    @GetMapping("products/{id}")
    Product findById(@PathVariable("id") Long id);

}
