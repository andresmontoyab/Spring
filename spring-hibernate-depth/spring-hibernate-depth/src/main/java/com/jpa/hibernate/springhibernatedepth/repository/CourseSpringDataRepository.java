package com.jpa.hibernate.springhibernatedepth.repository;

import com.jpa.hibernate.springhibernatedepth.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "/courses")
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);
    List<Course> findByNameAndId(String name, String id);
    List<Course> countByName(String name);
    List<Course> findByNameOrderByIdDesc(String name);
    List<Course> deleteByName(String name);

    @Query("Select c From Course c where name like '%50%'")
    List<Course> courseWith50InName();

    @Query(value = "Select c From Course c where name like '%50%'", nativeQuery = true)
    List<Course> courseWith50InNameUsingNativeQuery();
}
