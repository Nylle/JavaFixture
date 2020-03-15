package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Optional;

public class TestObjectWithGenerics {

    private List<TestObjectGeneric<String, Integer>> generics;

    private TestObjectGeneric<String, Integer> generic;

    private Class<Object> aClass;

    private Optional<Boolean> optional;

    public List<TestObjectGeneric<String, Integer>> getGenerics() {
        return generics;
    }

    public TestObjectGeneric<String, Integer> getGeneric() {
        return generic;
    }

    public Class<Object> getAClass() {
        return aClass;
    }

    public Optional<Boolean> getOptional() {
        return optional;
    }
}
