package com.github.nylle.javafixture.testobjects.abstractclasses;

public class GenericAbstractClassTUWithGenericImplementationTUImpl<T, U> extends GenericAbstractClassTUWithGenericImplementationTU<T, U> {
    private T t;
    private U u;

    @Override
    public T getT() {
        return t;
    }

    @Override
    public void setT(T value) {
        t = value;
    }

    @Override
    public U getU() {
        return u;
    }
}
