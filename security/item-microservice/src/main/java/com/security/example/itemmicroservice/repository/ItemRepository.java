package com.security.example.itemmicroservice.repository;

import com.security.example.itemmicroservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {
}
