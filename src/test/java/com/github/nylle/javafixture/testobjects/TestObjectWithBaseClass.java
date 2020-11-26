package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Map;

public class TestObjectWithBaseClass extends TestObject {

    private String field;

    public TestObjectWithBaseClass(String value, List<Integer> integers, Map<Integer, String> strings) {
        super(value, integers, strings);
    }

    public String getField() {
        return field;
    }
}

