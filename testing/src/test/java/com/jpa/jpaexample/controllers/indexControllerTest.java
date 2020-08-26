package com.jpa.jpaexample.controllers;

import com.jpa.jpaexample.model.Recipe;
import com.jpa.jpaexample.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class indexControllerTest {

    indexController indexController;
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController = new indexController(recipeService);
    }


    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/")).
                andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void indexPage() {

        // given
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(1l);
        Recipe recipe2 = new Recipe();
        recipe1.setId(2l);
        Recipe recipe3 = new Recipe();
        recipe1.setId(3l);
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        when(recipeService.getRecipe()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = indexController.IndexPage(model);

        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipe();
        verify(model, times(1)).addAttribute(anyString(), argumentCaptor.capture());
        Set<Recipe> indRecipes = argumentCaptor.getValue();
        assertEquals(2, indRecipes.size());


    }
}