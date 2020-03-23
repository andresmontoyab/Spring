package com.jpa.hibernate.springhibernatedepth.repository;


import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.entity.Passport;
import com.jpa.hibernate.springhibernatedepth.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StudentRepository {

    @Autowired
    EntityManager entityManager;


    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public void saveStudentWithPassport() {
        Passport passport = new Passport("Z123456");
        Student student = new Student("Mike");
        entityManager.persist(passport);
        student.setPassport(passport);
        entityManager.persist(student);
    }

    public void deleteById(Long id) {
        Student student = findById(id);
        entityManager.remove(student);
    }


    public void playWithEntityManager() {
        Student student = new Student("New student Example");
        entityManager.persist(student);          // Create new Object in the DB
        Student anotherStudent = new Student("Another student");
        entityManager.persist(anotherStudent);
        entityManager.flush();
        entityManager.clear();
        anotherStudent.setName("Another student - Updated");
        student.setName("New student Example - Updated");
    }

    public void insertStudentAndCourse(){
        // Create objects
        Student student = new Student("Jack");
        Course course = new Course("Microservices in 1000 steps");

        // Getting sequence from hibernate.
        entityManager.persist(student);
        entityManager.persist(course);

        // Setup relation
        student.addCourse(course);
        course.addStudent(student);

        // update the relation in db, in this point is when the record are going to be inserted by hibernate.
        entityManager.persist(student);

        // only at the end hibernate is going to insert the records
    }

    public void insertCourseToAStudent(Long id) {
        Student student = entityManager.find(Student.class, id);
        Course course = new Course("Trying to add a new course");
        student.addCourse(course);
        entityManager.persist(course);
        entityManager.persist(student);
    }

    public List<Student> retrieveStudentWithPassport() {
        TypedQuery<Student> query = entityManager.createQuery("Select s from Student s where s.passport.number like '%11%'", Student.class);
        return query.getResultList();
    }


}

