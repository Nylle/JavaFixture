package com.github.nylle.javafixture.testobjects;

import java.util.List;

public class TestObjectWithGenericCollection {

    private List<TestObjectGeneric<String, String>> generics;

    public List<TestObjectGeneric<String, String>> getGenerics() {
        return generics;
    }

    public void setGenerics(final List<TestObjectGeneric<String, String>> generics) {
        this.generics = generics;
    }
}
