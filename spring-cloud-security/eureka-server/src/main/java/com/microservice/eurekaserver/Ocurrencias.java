package com.microservice.eurekaserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Ocurrencias {

    public static void main(String[] args) {
        int[] myArray = {1,2,2,4,5,6,7,8,8,8,9,9,9};
        Map<Integer, Integer> ocurrenciesMap = new HashMap<>();
        for (int value : myArray) {
            if (ocurrenciesMap.get(value) == null) {
                ocurrenciesMap.put(value, 1);
            } else {
                Integer amount = ocurrenciesMap.get(value);
                ocurrenciesMap.put(value, ++amount);
            }
        }

        int maxValue = 0;
        Set<Integer> values = ocurrenciesMap.keySet();
        for (Integer value : values) {
            Integer amountPerValue = ocurrenciesMap.get(value);
            if(amountPerValue > maxValue) maxValue = value;
        }


        System.out.println("Longest : " + maxValue);
        System.out.println("Amount : " + ocurrenciesMap.get(maxValue));


    }

    private static class Ocurrencies {
        Integer number;
        Integer amount;

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

}
