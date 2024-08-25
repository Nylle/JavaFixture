package com.github.nylle.javafixture.testobjects.withconstructor;

public class ConstructorExceptionAndThrowingFactoryMethod {
    public ConstructorExceptionAndThrowingFactoryMethod() {
        throw new IllegalArgumentException("expected for tests");
    }
    public static ConstructorExceptionAndThrowingFactoryMethod factoryMethod() {
        throw new IllegalStateException("expected for tests");
    }
}
