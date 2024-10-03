package com.github.nylle.javafixture.testobjects.withconstructor;

public class ConstructorExceptionAndThrowingFactoryMethod {
    public ConstructorExceptionAndThrowingFactoryMethod() {
        throw new IllegalArgumentException("constructor exception expected for tests");
    }
    public static ConstructorExceptionAndThrowingFactoryMethod factoryMethod() {
        throw new IllegalStateException("factory method exception expected for tests");
    }
}
