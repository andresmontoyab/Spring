package com.microservices.microservicesproducts.domain.repositories;

import com.microservices.microservicesproducts.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
