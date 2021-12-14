package com.github.nylle.javafixture.specimen.constraints;


public class StringConstraints {

    private final int min;
    private final int max;

    public StringConstraints(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
