package com.microservices.microservicesproducts.domain.repositories;

import com.spring.commons.appcommons.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
