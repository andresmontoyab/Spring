package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;
import com.microservices.microservicesitems.domain.Product;
import com.microservices.microservicesitems.feign.FeignClientProducts;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceFeignImpl implements ItemService {

    private final FeignClientProducts clientProducts;

    public ItemServiceFeignImpl(FeignClientProducts clientProducts) {
        this.clientProducts = clientProducts;
    }

    @Override
    public List<Item> findAll() {
        List<Product> products = clientProducts.findAll();
        return products.stream()
                .map(product -> new Item(product,1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer amount) {
        Product product = clientProducts.findById(id);
        return new Item(product,amount);
    }

    @Override
    public Item findByIdtimeout() {
        return null;
    }
}
