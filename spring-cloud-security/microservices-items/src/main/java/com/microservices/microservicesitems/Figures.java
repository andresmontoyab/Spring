package com.microservices.microservicesitems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Figures {
    Double calculeVolume();
}

/**
 * Example Constructor Chainig
 */
class box implements Figures {

    private Double width;
    private Double height;
    private Double deep;

    private Comparator<box> maxVolume = (o1, o2) -> {
        if(o1.calculeVolume() > o2.calculeVolume()) {
            return 1;
        }

        if(o1.calculeVolume() < o2.calculeVolume()) {
            return -1;
        }

        return 0;
    };

    public box(Double width, Double height, Double deep) {
        this.width = width;
        this.height = height;
        this.deep = deep;
    }

    public box(Double width, Double height) {
        this(height);
        this.width = width;
    }

    public box(Double height) {
        this.height = height;
    }

    @Override
    public Double calculeVolume() {
        return width*height*deep;
    }


}

class init {

    private static Comparator<box> maxVolume = (o1, o2) -> {
        if(o1.calculeVolume() > o2.calculeVolume()) {
            return 1;
        }

        if(o1.calculeVolume() < o2.calculeVolume()) {
            return -1;
        }

        return 0;
    };


    private static Comparator<box> mixVolume = (o1, o2) -> {
        if(o1.calculeVolume() > o2.calculeVolume()) {
            return -1;
        }

        if(o1.calculeVolume() < o2.calculeVolume()) {
            return 1;
        }

        return 0;
    };
    public static void main(String[] args) {
        box box1 = new box(Double.valueOf(1),Double.valueOf(3),Double.valueOf(3));
        box box2 = new box(Double.valueOf(1),Double.valueOf(3),Double.valueOf(4));
        List<box> boxes = new ArrayList<>();
        boxes.add(box1);
        boxes.add(box2);

        Stream.of(1,2,3,4,5);
        Arrays.asList(1,2,3,4,5).stream().count();

        boxes.stream()
                .sorted(maxVolume)
                .forEach((box) -> System.out.println(box.calculeVolume()));

        boxes.stream()
                .sorted(mixVolume)
                .forEach((box) -> System.out.println(box.calculeVolume()));

    }
}
