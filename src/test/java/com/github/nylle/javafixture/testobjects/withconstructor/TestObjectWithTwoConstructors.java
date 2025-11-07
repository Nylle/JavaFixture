package com.github.nylle.javafixture.testobjects.withconstructor;

public class TestObjectWithTwoConstructors {
    private String value;
    private String other;

    public TestObjectWithTwoConstructors(String value, String other) {
        this.value = value;
        this.other = other;
    }

    public TestObjectWithTwoConstructors(String value) {
        this.value = value;
        this.other = "default";
    }

    public String getValue() {
        return value;
    }

    public String getOther() {
        return other;
    }
}
