package com.microservices.microservicesproducts.service;

import com.microservices.microservicesproducts.domain.entities.Product;

import java.util.List;

public interface IProductService {

    public List<Product> findAll();

    public Product findById(Long id);
}
