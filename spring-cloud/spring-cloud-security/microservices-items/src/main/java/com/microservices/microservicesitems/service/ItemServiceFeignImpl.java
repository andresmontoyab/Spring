package com.microservices.microservicesitems.service;

import com.microservices.microservicesitems.domain.Item;
import com.spring.commons.appcommons.models.entity.Product;
import com.microservices.microservicesitems.feign.FeignClientProducts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
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

    @Override
    public Product save(Product product) {
        return clientProducts.createProduct(product);
    }

    @Override
    public Product update(Product product, Long id) {
        return clientProducts.updateProduct(product, id);
    }

    @Override
    public void delete(Long id) {
        clientProducts.deleteProduct(id);
    }
}
