package com.github.nylle.javafixture.testobjects;

public class TestObjectWithAllConstructors {
    private String value;

    private TestObjectWithAllConstructors(String value, String ignored) {
        this.value = "default";
    }

    protected TestObjectWithAllConstructors(String value) {
        this.value = value;
    }

    public TestObjectWithAllConstructors() {
        this.value = "default";
    }

    public String getStrings() {
        return value;
    }
}
