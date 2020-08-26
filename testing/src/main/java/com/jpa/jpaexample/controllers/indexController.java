package com.jpa.jpaexample.controllers;
import com.jpa.jpaexample.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class indexController {

    private final RecipeService recipeService;


    public indexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public String IndexPage(Model model) {
        log.debug("Im in the controller");
        model.addAttribute("recipes", recipeService.getRecipe());

        return "index";
    }
}
