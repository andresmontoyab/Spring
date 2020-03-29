package com.jpa.hibernate.springhibernatedepth.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String line1;
    private String line2;
    private String city;

    public Address() {
    }
}
