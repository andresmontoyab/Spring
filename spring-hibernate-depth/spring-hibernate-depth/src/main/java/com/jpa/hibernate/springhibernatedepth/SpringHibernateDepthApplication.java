package com.jpa.hibernate.springhibernatedepth;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringHibernateDepthApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CourseRepository courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringHibernateDepthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //FindById
        Course course = courseRepository.findById(1001l);
        logger.info("This is the course with 1001 {}", course);

        //DeleteById
        logger.info("Deleting by Id");
        courseRepository.deleteById(1003l);

        //Save Course
        logger.info("Saving Microservices Course: {}",
                courseRepository.save(new Course("Microservices in 100 Steps")));

        //Testing lastUpdatedDate and createdDate
        logger.info("Testing last update and created date ");
        Course datesTesting = courseRepository.findById(1001l);
        datesTesting.setName("Testing Dates - Updated");
        courseRepository.save(datesTesting);


    }
}
