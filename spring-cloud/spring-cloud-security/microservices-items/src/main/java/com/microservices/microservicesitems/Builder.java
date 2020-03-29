package com.microservices.microservicesitems;

public class Builder {

    public static void main(String[] args) {

    }
}


class Toy  {
    private String head;
    private String legs;
    private String hands;

    private static Toy toy;

    private Toy(String head, String legs, String hands) {
        this.head = head;
        this.legs = legs;
        this.hands = hands;
    }

    public static synchronized Toy getInstance() {
        if(toy == null) {
            toy = new Toy("head", "legss", "handss");
        }
        return toy;
    }
}