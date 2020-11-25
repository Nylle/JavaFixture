package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Map;

public class TestObjectWithBaseClass extends TestObject {

    private String localField;

    public TestObjectWithBaseClass(String value, List<Integer> integers, Map<Integer, String> strings) {
        super(value, integers, strings);
    }

    public String getLocalField() {
        return localField;
    }
}
