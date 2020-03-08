package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.entity.Passport;
import com.jpa.hibernate.springhibernatedepth.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class StudentRepository {

    @Autowired
    EntityManager entityManager;

    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public Student save(Student Student) {
        if(Student.getId() == null) {
            //insert
            entityManager.persist(Student);
        }  else {
            //update
            entityManager.merge(Student);
        }
        return Student;
    }

    public void saveStudentWithPassport() {
        Passport passport = new Passport("z1234");
        entityManager.persist(passport);
        Student student = new Student("Andres New Course");
        student.setPassport(passport);
        entityManager.persist(student);
    }

    public List<Student> selectWithJPQL() {
        String jpql = "Select c from Student c";
        Query query = entityManager.createNamedQuery("query_get_all_Students");
        //Query query = entityManager.createQuery(jpql);
        return (ArrayList) query.getResultList();

    }

    public void deleteById(Long id) {
        Student Student = findById(id);
        entityManager.remove(Student);
    }
    public void playWithEntityManager() {

    }
}
