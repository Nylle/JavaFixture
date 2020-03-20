package com.github.nylle.javafixture.testobjects;

import java.util.Optional;

public class TestObjectWithGenericConstructor {
    private final String privateField = null;

    private final String value;

    private final Optional<Integer> integer;

    public TestObjectWithGenericConstructor(String value, Optional<Integer> integer) {
        this.value = value;
        this.integer = integer;
    }

    public String getValue() {
        return value;
    }

    public Optional<Integer> getInteger() {
        return integer;
    }

    public String getPrivateField() {
        return privateField;
    }
}
