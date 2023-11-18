package com.github.nylle.javafixture.testobjects.abstractclasses;

public class AbstractClassWithGenericImplementationImpl<T> extends AbstractClassWithGenericImplementation {
    private T t;
    private String string;

    @Override
    public String getString() {
        return string;
    }

    public T getT() {
        return t;
    }
}
