package com.github.nylle.javafixture.testobjects;

import java.util.List;

public class TestObjectWithGenericCollection {

    private List<TestObjectGeneric<String, Integer>> generics;

    public List<TestObjectGeneric<String, Integer>> getGenerics() {
        return generics;
    }

    public void setGenerics(final List<TestObjectGeneric<String, Integer>> generics) {
        this.generics = generics;
    }
}
