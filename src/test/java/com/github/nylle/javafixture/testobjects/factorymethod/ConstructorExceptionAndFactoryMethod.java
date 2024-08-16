package com.github.nylle.javafixture.testobjects.factorymethod;

public class ConstructorExceptionAndFactoryMethod {
    private int value;

    public ConstructorExceptionAndFactoryMethod() {
        throw new IllegalArgumentException("expected for tests");
    }

    private ConstructorExceptionAndFactoryMethod(int value) {
        this.value = value;
    }

    public static ConstructorExceptionAndFactoryMethod factoryMethod() {
        return new ConstructorExceptionAndFactoryMethod(42);
    }

    public int getValue() {
        return value;
    }
}
