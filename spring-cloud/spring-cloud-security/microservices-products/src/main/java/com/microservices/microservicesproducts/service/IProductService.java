package com.microservices.microservicesproducts.service;


import com.spring.commons.appcommons.models.entity.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    public void deleteById(Long id);
}
