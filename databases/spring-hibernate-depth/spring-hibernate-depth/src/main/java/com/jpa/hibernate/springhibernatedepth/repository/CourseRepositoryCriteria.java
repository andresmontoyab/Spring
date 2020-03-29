package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CourseRepositoryCriteria {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager entityManager;

    public void getAllCourses() {
        // Step 1 -> Select c from Course c;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        //Step 4

        //Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with Criteria Query -> {}", courses);
    }

    public void getAllCoursesRelatedWithSpring() {
        // Step 1 -> Select c From Course c where name like '%Spring%'
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Predicate likeSpring = criteriaBuilder.like(courseRoot.get("name"), "%Spring%");

        //Step 4
        criteriaQuery.where(likeSpring);

        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with word Spring and with Criteria Query -> {}", courses);
    }

    public void getAllCoursesWithoutStudents() {
        // Step 1 -> Select c From Course c where c.students is empty
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Predicate studentIsEmpty = criteriaBuilder.isEmpty(courseRoot.get("students"));

        //Step 4
        criteriaQuery.where(studentIsEmpty);

        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with no students and with Criteria Query -> {}", courses);
    }

    public void getAllCoursesJoinWithStudents() {
        // Step 1 -> Select c From Course c where c JOIN c.students
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Join<Object, Object> join = courseRoot.join("students", JoinType.LEFT);

        //Step 4
        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses Join with students and with Criteria Query -> {}", courses);
    }
}
