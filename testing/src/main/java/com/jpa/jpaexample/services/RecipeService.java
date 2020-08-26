package com.jpa.jpaexample.services;

import com.jpa.jpaexample.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface RecipeService {

    Set<Recipe> getRecipe();

    Recipe getRecipeById(Long id);
}
