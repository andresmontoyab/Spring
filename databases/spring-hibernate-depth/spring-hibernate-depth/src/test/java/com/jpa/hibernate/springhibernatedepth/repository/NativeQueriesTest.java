package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.SpringHibernateDepthApplication;
import com.jpa.hibernate.springhibernatedepth.entity.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringHibernateDepthApplication.class)
public class NativeQueriesTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager entityManager;

    @Test
    public void nativeQueryFindAllTest() {
        List<Course> courses = entityManager.createNativeQuery("Select * from Course ", Course.class).getResultList();
        logger.info("The result of the native query {}", courses);
    }

    @Test

    public void nativeQuery_withParameters() {
        Query nativeQuery = entityManager.createNativeQuery("Select * from Course where id = ?", Course.class);
        nativeQuery.setParameter(1, 1001l);
        List<Course> courses = nativeQuery.getResultList();
        logger.info("The result of the native query with parameters {}", courses);
    }

    public void nativeQuery_withNamedParameters() {
        Query nativeQuery = entityManager.createNativeQuery("Select * from Course where id = :id", Course.class);
        nativeQuery.setParameter("id", 1001l);
        List<Course> courses = nativeQuery.getResultList();
        logger.info("The result of the native query with native parameters {}", courses);
    }
}