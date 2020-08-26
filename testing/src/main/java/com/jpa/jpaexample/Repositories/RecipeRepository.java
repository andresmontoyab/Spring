package com.jpa.jpaexample.Repositories;

import com.jpa.jpaexample.model.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<Recipe> findById(Long id);
}
