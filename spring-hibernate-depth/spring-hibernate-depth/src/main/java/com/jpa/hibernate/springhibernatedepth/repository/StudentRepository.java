package com.jpa.hibernate.springhibernatedepth.repository;


import com.jpa.hibernate.springhibernatedepth.entity.Passport;
import com.jpa.hibernate.springhibernatedepth.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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

}

