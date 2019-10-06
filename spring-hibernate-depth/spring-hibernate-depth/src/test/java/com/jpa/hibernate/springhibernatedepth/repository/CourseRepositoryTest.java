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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringHibernateDepthApplication.class)
public class CourseRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CourseRepository courseRepository;

    @Test
    public void findById_basic() {
        //given
        Long id = 1101l;

        //when
        Course course = courseRepository.findById(id);

        //then
        assertEquals(id, course.getId());
        assertEquals("JPA in 50 steps", course.getName());
    }

    @Test
    public void findById_wrongName() {
        //given
        Long id = 1101l;

        //when
        Course course = courseRepository.findById(id);

        //then
        assertEquals(id, course.getId());
        assertNotEquals("Spring in 50 steps", course.getName());
    }


    @Test
    @DirtiesContext // Reset de data that was affected.
    public void deleteById() {
        //given
        Long id = 1102l;

        //when
        courseRepository.deleteById(id);
        Course course = courseRepository.findById(id);

        //then
        assertNull(course);
    }

    @Test
    @DirtiesContext // Reset de data that was affected.
    public void saveCourseTest() {
        //given
        Long id = 1101l;
        Course initialState = courseRepository.findById(id);
        initialState.setName(initialState.getName().concat("- Updated"));

        //when
        courseRepository.save(initialState);

        //then
        Course finalCourseState = courseRepository.findById(id);
        assertEquals(id, finalCourseState.getId());
        assertEquals(initialState.getName(), finalCourseState.getName());
    }

    @Test
    public void playWithentityManagerTest() {
        courseRepository.playWithEntityManager();
    }
}