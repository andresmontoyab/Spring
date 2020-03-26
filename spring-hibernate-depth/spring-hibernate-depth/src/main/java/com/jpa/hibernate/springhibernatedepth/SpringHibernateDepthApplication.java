package com.jpa.hibernate.springhibernatedepth;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.entity.FullTimeEmployee;
import com.jpa.hibernate.springhibernatedepth.entity.PartTimeEmployee;
import com.jpa.hibernate.springhibernatedepth.repository.CourseRepository;
import com.jpa.hibernate.springhibernatedepth.repository.CourseRepositoryCriteria;
import com.jpa.hibernate.springhibernatedepth.repository.CourseSpringDataRepository;
import com.jpa.hibernate.springhibernatedepth.repository.EmployeeRepository;
import com.jpa.hibernate.springhibernatedepth.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@SpringBootApplication
public class SpringHibernateDepthApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CourseRepositoryCriteria courseRepositoryCriteria;

    @Autowired
    CourseSpringDataRepository courseSpringDataRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringHibernateDepthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //basicJpaWithCourseRepository();
        //JpaRelationShips();
        //JpaInheritance();
        //workingWithJpql();
        //JpaWithCriteriaApi();
        //playingWithSpringData();
        //firstLevelCache();

    }


    @Transactional
    private void firstLevelCache() {
        logger.info("Calling First Time");
        courseRepository.findById(1001L);
        logger.info("Calling Second Time");
        courseRepository.findById(1001L);
        logger.info("Calling Time Time");
        courseRepository.findById(1001L);
    }

    private void playingWithSpringData() {
        Course newCourse = new Course("New Spring Data Course");
        courseSpringDataRepository.save(newCourse);
        newCourse.setName("New Spring Data Course - Updated");
        courseSpringDataRepository.save(newCourse);
        logger.info("Amount of courses is {}", courseSpringDataRepository.count());
        logger.info("All courses -> {}", courseSpringDataRepository.findAll());

        Sort sort = new Sort(Sort.Direction.DESC, "name");
        logger.info("All courses sorted -> {}", courseSpringDataRepository.findAll(sort));

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Course> firstPage = courseSpringDataRepository.findAll(pageRequest);
        logger.info("First Page -> {}", firstPage.getContent());

        Pageable pageable = firstPage.nextPageable();
        Page<Course> secondPage = courseSpringDataRepository.findAll(pageable);
        logger.info("Second Page -> {}", secondPage.getContent());

        logger.info("Course with name JPA in 50 steps -> {}", courseSpringDataRepository.findByName("JPA in 50 steps"));
    }

    private void JpaWithCriteriaApi() {
        courseRepositoryCriteria.getAllCourses();
        courseRepositoryCriteria.getAllCoursesRelatedWithSpring();
        courseRepositoryCriteria.getAllCoursesWithoutStudents();
        courseRepositoryCriteria.getAllCoursesJoinWithStudents();
    }

    private void workingWithJpql() {
        courseRepository.retrieveCoursesWithoutStudents();
        courseRepository.retrieveCoursesWithMoreThanTwoStudents();
        courseRepository.retrieveCoursesOrderBySize();
        logger.info("Student with passport patter 11 {}",studentRepository.retrieveStudentWithPassport());
        courseRepository.join();
    }

    private void JpaInheritance() {
        logger.info("*****************//// Working with Employees //////////***************");
        employeeRepository.insert(new FullTimeEmployee("Jack", new BigDecimal(10000)));
        employeeRepository.insert(new PartTimeEmployee("Jill", new BigDecimal(50)));
        logger.info("Employees {}", employeeRepository.retrieveAllFullTimeEmployees());
    }

    private void JpaRelationShips() {
        logger.info("*****************//// Begin to test Relationships //////////***************");
        logger.info("*****************//// One to One Relationship //////////***************");
        studentRepository.saveStudentWithPassport();


        logger.info("*****************//// Many to One Relationship //////////***************");
        logger.info("Adding Review");
        courseRepository.addReviewsForCourse();
        logger.info("Deleting Review");
        courseRepository.deleteTheFirstReview();

        logger.info("*****************//// Many to Many Relationship //////////***************");
        studentRepository.insertStudentAndCourse();
        studentRepository.insertCourseToAStudent(2001L);
    }

    private void basicJpaWithCourseRepository() {
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

        logger.info("Testing select all with JPQL");
        courseRepository.selectWithJPQL().forEach((courseByJPQL) -> System.out.println(courseByJPQL));
    }
}
