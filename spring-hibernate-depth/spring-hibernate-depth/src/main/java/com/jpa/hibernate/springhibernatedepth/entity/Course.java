package com.jpa.hibernate.springhibernatedepth.entity;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDateTime;

@Entity
@NamedQueries(value = {
        @NamedQuery(name="query_get_all_courses", query="Select c from Course c"),
        @NamedQuery(name="query_get_all_courses_copy", query="Select c from Course c")
})
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @CreatedDate
    private LocalDateTime createdDate;

    protected Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
