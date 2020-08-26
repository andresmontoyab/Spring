package com.jpa.jpaexample.Repositories;

import com.jpa.jpaexample.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByDescription( String desciption);

}
