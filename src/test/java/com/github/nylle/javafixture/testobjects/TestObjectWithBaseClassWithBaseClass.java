package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Map;

public class TestObjectWithBaseClassWithBaseClass extends TestObjectWithBaseClass {

    private String localField;

    public TestObjectWithBaseClassWithBaseClass(final String value, final List<Integer> integers, final Map<Integer, String> strings) {
        super(value, integers, strings);
    }

    public String getField() {
        return localField;
    }
}
