package com.jpa.hibernate.springhibernatedepth.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class School {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    public School() {
    }

    public School(String name) {
        this.name = name;
    }
}
