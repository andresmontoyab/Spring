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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringHibernateDepthApplication.class)
public class StudentRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EntityManager entityManager; 

    @Test
    public void retrieveStudentAndPassport() {

    }


}