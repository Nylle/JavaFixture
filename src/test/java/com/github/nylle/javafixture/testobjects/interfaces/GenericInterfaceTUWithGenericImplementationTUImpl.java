package com.github.nylle.javafixture.testobjects.interfaces;

public class GenericInterfaceTUWithGenericImplementationTUImpl<T, U> implements GenericInterfaceTUWithGenericImplementationTU<T, U> {
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
