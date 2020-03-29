package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class CourseRepository {

    @Autowired
    EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Course findById(Long id) {
        return entityManager.find(Course.class, id);
    }

    public Course save(Course course) {
        if(course.getId() == null) {
            //insert
            entityManager.persist(course);
        }  else {
            //update
            entityManager.merge(course);
        }
        return course;
    }

    public List<Course> selectWithJPQL() {
        String jpql = "Select c from Course c";
        Query query = entityManager.createNamedQuery("query_get_all_courses");
        //Query query = entityManager.createQuery(jpql);
        return (ArrayList) query.getResultList();

    }

    public void retrieveCoursesWithStudent() {
        EntityGraph<Course> entityGraph = entityManager.createEntityGraph(Course.class);
        entityGraph.addSubgraph("students");
        List<Course> courses = entityManager.createNamedQuery("query_get_all_courses", Course.class)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();
        courses.forEach(course -> logger.info("Course -> {} Students -> {}", course, course.getStudents()));
    }

    public void deleteById(Long id) {
        Course course = findById(id);
        entityManager.remove(course);
    }

    /**
     *  Play with Entity Manager
     *  Lesson 1: If you're inside a transation and you're managing something with the
     *  entity manager, updating, adding, deleting that particular thing continue being
     *  managed by the entity manager until the end of the transaction.
     *
     *  Lesson 2.
     *  the method flush() save all the information or operation until that point.
     *
     *  Lesson 3.
     *  The method detach(Object o) help us to remove the track of a certaint object,
     *  if we detach one object the entity manager is not going to manage that object anymore.
     *
     *  Lesson 4.
     *  The clear() method clean all the operation until that point, for example if we do not add any flush()
     *  and use clear, nothing in the db is going to be updated because is a transanction.
     *
     *  Lesson 5.
     *  the refresh() method help us to recovery the entity information from the DB
     *  Example step1: persist(), step2: flush(), step3, setValue(), step4 refresh()
     *  If the refresh method is delete it the final info in the db is going to be the info
     *  in the setValue, but because we are using the refresh the final info is the original.
     *
      */
    public void playWithEntityManager() {
        Course course = new Course("New course Example");
        entityManager.persist(course);          // Create new Object in the DB
        Course anotherCourse = new Course("Another course");
        entityManager.persist(anotherCourse);

        entityManager.flush();

        entityManager.clear();
//        entityManager.detach(course);
//        entityManager.detach(anotherCourse);
        anotherCourse.setName("Another course - Updated");
        course.setName("New course Example - Updated");  // Because this is a transanction every single change in the app is going to update the database too, so is not require to call merge.

    }

    /**
     * JPQL Java Persistance Query Language
     * Jpql is a way of write queries using entities.
     */

    /**
     * NamedQueries help us to re use JQPL queries and those
     * queries are written in the entitye
     *
     * 1. If you need to define one query use the @NamedQuery Annotations
     * 2. If you need to define several queries use the @NamedQueries
     */

    public void addReviewsForCourse() {
        Course course = findById(1001L);
        logger.info("course.getReviews -> {}", course.getReviews());
        //Creating Reviews
        Review review1 = new Review("5", "This one is a new description");
        Review review2 = new Review("5", "and here we again.");

        // Setting relationships
        review1.setCourse(course);
        course.addReview(review1);
        course.addReview(review2);
        review2.setCourse(course);

        // Adding review to the db
        entityManager.persist(review1);
        entityManager.persist(review2);
        logger.info("course.getReviews -> {}", course.getReviews());
    }

    public void deleteTheFirstReview() {
        Course course = findById(1001L);
        logger.info("course.getReviews -> {}", course.getReviews());
        List<Review> reviews = course.getReviews();
        Review review = reviews.get(0);
        course.removeReview(review);
        entityManager.merge(course);
        logger.info("course.getReviews -> {}", course.getReviews());
    }

    public void retrieveCoursesWithoutStudents() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c where c.students is empty", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    public void retrieveCoursesWithMoreThanTwoStudents() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c where size(c.students) >= 2", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    public void retrieveCoursesOrderBySize() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c order by size(c.students)", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    // JOIN -> Select c,s from Course c JOIN c.students s;
    // LEFT JOIN -> Select c,s from Course c LEFT JOIN c.students s;
    // CROSS JOIN -> Select c,s from Course c CROSS JOIN c.students s;
    public void join() {
        Query query = entityManager.createQuery("Select c,s from Course c JOIN c.students s");
        List<Object[]> resultList = query.getResultList();
        logger.info("JOIN Course size-> {}", resultList.size());
        for (Object[] result : resultList) {
            logger.info("Course {} Student {}", result[0], result[1]);
        }
    }
}
