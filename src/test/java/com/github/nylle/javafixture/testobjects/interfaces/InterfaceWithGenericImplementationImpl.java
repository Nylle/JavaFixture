package com.github.nylle.javafixture.testobjects.interfaces;

public class InterfaceWithGenericImplementationImpl<T> implements InterfaceWithGenericImplementation {
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
