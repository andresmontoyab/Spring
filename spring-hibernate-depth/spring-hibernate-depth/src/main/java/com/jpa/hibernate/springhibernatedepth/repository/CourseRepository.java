package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CourseRepository {

    @Autowired
    EntityManager entityManager;

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
}
