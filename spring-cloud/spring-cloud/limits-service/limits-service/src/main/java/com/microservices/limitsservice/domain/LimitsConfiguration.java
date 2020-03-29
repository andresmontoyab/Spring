package com.microservices.limitsservice.domain;

public class LimitsConfiguration {

    private int maximun;
    private int minimum;

    public LimitsConfiguration() {
    }

    public LimitsConfiguration(int maximun, int minimum) {
        this.maximun = maximun;
        this.minimum = minimum;
    }

    public int getMaximun() {
        return maximun;
    }

    public void setMaximun(int maximun) {
        this.maximun = maximun;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}
