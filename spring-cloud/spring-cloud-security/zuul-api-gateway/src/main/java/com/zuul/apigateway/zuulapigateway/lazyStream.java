package com.zuul.apigateway.zuulapigateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lazyStream {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> numbersTimeTwo = new ArrayList<>();

        numbers.stream().map(x -> numbersTimeTwo.add(x * 2));

        numbersTimeTwo.forEach(x -> System.out.println(x));
    }
}
