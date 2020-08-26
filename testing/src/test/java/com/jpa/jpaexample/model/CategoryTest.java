package com.jpa.jpaexample.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {

    Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void getId(){
        Long idValue = 4l;
        category.setId(4l);
        assertEquals(idValue, category.getId());
    }
}