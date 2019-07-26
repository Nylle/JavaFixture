package com.github.nylle.javafixture.testobjects;

import java.util.List;

public class TestObject {
    private final String value;

    private final List<Integer> integers;

    public TestObject(String value, final List<Integer> integers) {
        this.value = value;
        this.integers = integers;
    }

    public String getValue() {
        return value;
    }

    public List<Integer> getIntegers() {
        return integers;
    }
}
