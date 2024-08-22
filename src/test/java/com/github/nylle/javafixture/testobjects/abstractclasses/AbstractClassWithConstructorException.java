package com.github.nylle.javafixture.testobjects.abstractclasses;

public abstract class AbstractClassWithConstructorException {
    public AbstractClassWithConstructorException() {
        throw new IllegalArgumentException("expected for tests");
    }
}
