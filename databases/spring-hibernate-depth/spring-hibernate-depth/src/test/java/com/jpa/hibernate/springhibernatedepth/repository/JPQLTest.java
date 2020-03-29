package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.SpringHibernateDepthApplication;
import com.jpa.hibernate.springhibernatedepth.entity.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringHibernateDepthApplication.class)
public class JPQLTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager entityManager;

    @Test
    public void jqplFindAllTest() {
        List<Course> courses = entityManager.createQuery("Select c from Course c").getResultList();
        logger.info("The result of the jqpl {}", courses);
    }

    @Test
    public void jqplFindAllTest_typed() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c", Course.class); // Typed query is better because you can specifc the return type
        //TypedQuery<Course> query = entityManager.createNamedQuery("query_get_all_courses", Course.class);
        List<Course> courses = query.getResultList();
        logger.info("The result of the jqpl {}", courses);

    }

    @Test
    public void jqplFindAllTest_namedQuery() {
        TypedQuery<Course> query = entityManager.createNamedQuery("query_get_all_courses_copy", Course.class);
        List<Course> courses = query.getResultList();
        logger.info("The result of the jqpl {}", courses);

    }

}