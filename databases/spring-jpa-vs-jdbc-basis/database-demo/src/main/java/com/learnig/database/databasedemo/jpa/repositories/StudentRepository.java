package com.learnig.database.databasedemo.jpa.repositories;

import com.learnig.database.databasedemo.jpa.entity.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StudentRepository {

    /**
     *  Entity manager, manage the entities, that means all the operation that you need to perform in a
     *  specific session.
     *  EntityManager is the interface of the persistence context.
     */
    @PersistenceContext
    EntityManager entityManager;

    public Student findById(Integer id) {
        return entityManager.find(Student.class, id);
    }

    /**
     * merge method does -> ignore wether the id is set inside the entity.
     *
     * @param student
     * @return
     */
    public Student updateStudent(Student student) {
        return entityManager.merge(student);
    }

    public Student createStudent(Student student) {
        return entityManager.merge(student);
    }

    public void deleteById(Integer id) {
        Student student = findById(id); // I need to get the object first
        entityManager.remove(student); // The auto generated id.
    }

    public List<Student> findAll() {
        TypedQuery<Student> namedQuery = entityManager.createNamedQuery("find_all_students", Student.class);
        return namedQuery.getResultList();
    }

}
