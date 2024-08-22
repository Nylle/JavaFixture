package com.github.nylle.javafixture.testobjects.withconstructor;

public class ConstructorExceptionAndNoFactoryMethod {
    public ConstructorExceptionAndNoFactoryMethod() {
        throw new IllegalArgumentException("expected for tests");
    }
}
