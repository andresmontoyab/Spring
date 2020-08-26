package com.jpa.jpaexample.bootstrap;

import com.jpa.jpaexample.Repositories.CategoryRepository;
import com.jpa.jpaexample.Repositories.RecipeRepository;
import com.jpa.jpaexample.Repositories.UnitOfMeasureRepository;
import com.jpa.jpaexample.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent>{

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap Data");
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        // Get OUMS

        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");

        if (!eachUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> tablespoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");

        if (!tablespoonUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }


        Optional<UnitOfMeasure> teaspoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        if (!teaspoonUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");

        if (!dashUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");

        if (!pintUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription("Cup");

        if (!cupUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tablespoonUom = tablespoonUomOptional.get();
        UnitOfMeasure teaspoonUom = teaspoonUomOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();
        UnitOfMeasure pintUom = pintUomOptional.get();
        UnitOfMeasure cupUom = cupUomOptional.get();

        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");

        if (!americanCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category Not Found.");
        }

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");

        if (!mexicanCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category Not Found.");
        }

        Category mexicanCategory = mexicanCategoryOptional.get();
        Category americanCategory = americanCategoryOptional.get();

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Bla bla bla guacamole bla bla bla bla");

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Notes Guacamole bla bla bla bla");
        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);


        guacRecipe.getIngredents().add(new Ingredent("Step 1 --> Rice", new BigDecimal(2),eachUom, guacRecipe));
        guacRecipe.getIngredents().add(new Ingredent("Step 2 --> Salt", new BigDecimal(5),teaspoonUom,guacRecipe));
        guacRecipe.getIngredents().add(new Ingredent("Step 3 --> Red Onion", new BigDecimal(2),tablespoonUom,guacRecipe) );
        guacRecipe.getIngredents().add(new Ingredent("Step 4 --> Serrano Chiles", new BigDecimal(4),tablespoonUom, guacRecipe));

        guacRecipe.getCategories().add(mexicanCategory);
        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.setUrl("FirstTry.recipe1.com");
        guacRecipe.setSource("Sumply Recipe");
        guacRecipe.setServings(3);

        recipes.add(guacRecipe);


        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Perfect Tacos");
        tacosRecipe.setPrepTime(9);
        tacosRecipe.setCookTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);
        tacosRecipe.setDirections("Bla bla bla Tacos BLA BLa bla bla bla bla bla bla bla");

        Notes tacosNotes = new Notes();
        tacosNotes.setRecipeNotes("Notes Tacos bla bla bla bla");
        tacosNotes.setRecipe(tacosRecipe);
        tacosRecipe.setNotes(tacosNotes);

        tacosRecipe.getIngredents().add(new Ingredent("Step 1 --> Chili", new BigDecimal(2),tablespoonUom, tacosRecipe));
        tacosRecipe.getIngredents().add(new Ingredent("Step 2 --> Dried Oregano", new BigDecimal(1),teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredents().add(new Ingredent("Step 3 --> Dried Cumin ", new BigDecimal(2),teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredents().add(new Ingredent("Step 4 --> Sugar ", new BigDecimal(1),teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredents().add(new Ingredent("Step 5 --> Salt  ", new BigDecimal(.5),teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredents().add(new Ingredent("Step 6 --> Clove of Garlic  ", new BigDecimal(1),eachUom, tacosRecipe));

        tacosRecipe.getCategories().add(mexicanCategory);
        tacosRecipe.getCategories().add(americanCategory);
        tacosRecipe.setUrl("secondTry.com");
        tacosRecipe.setServings(5);
        tacosRecipe.setSource("Simply Recipe");

        recipes.add(tacosRecipe);
        return recipes;
    }
}
