package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Map;

public class TestObject {
    public final static String STATIC_FIELD = "unchanged";

    private final String value;

    private final List<Integer> integers;

    private final Map<Integer, String> strings;

    public TestObject(String value, final List<Integer> integers, Map<Integer, String> strings) {
        this.value = value;
        this.integers = integers;
        this.strings = strings;
    }

    public String getValue() {
        return value;
    }

    public List<Integer> getIntegers() {
        return integers;
    }

    public Map<Integer, String> getStrings() {
        return strings;
    }
}
