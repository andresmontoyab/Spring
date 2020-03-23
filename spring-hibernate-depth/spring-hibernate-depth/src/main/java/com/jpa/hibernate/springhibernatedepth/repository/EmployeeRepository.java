package com.jpa.hibernate.springhibernatedepth.repository;


import com.jpa.hibernate.springhibernatedepth.entity.Course;
import com.jpa.hibernate.springhibernatedepth.entity.Employee;
import com.jpa.hibernate.springhibernatedepth.entity.FullTimeEmployee;
import com.jpa.hibernate.springhibernatedepth.entity.Passport;
import com.jpa.hibernate.springhibernatedepth.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class EmployeeRepository {

    @Autowired
    EntityManager entityManager;

    public void insert(Employee employee) {
        entityManager.persist(employee);
    }

    public List<Employee> retrieveAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    public List<FullTimeEmployee> retrieveAllFullTimeEmployees() {
        return entityManager.createQuery("SELECT e FROM FullTimeEmployee e", FullTimeEmployee.class).getResultList();
    }

}

