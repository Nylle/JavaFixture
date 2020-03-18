package com.github.nylle.javafixture.testobjects;

public class TestObjectWithPrivateConstructor {

    private final String value;

    private TestObjectWithPrivateConstructor() {
        this.value = null;
    }

    protected TestObjectWithPrivateConstructor(String value) {
        this.value = value;
    }
}
