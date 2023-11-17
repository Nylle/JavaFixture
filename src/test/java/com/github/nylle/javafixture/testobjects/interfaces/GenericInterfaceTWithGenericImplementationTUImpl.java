package com.github.nylle.javafixture.testobjects.interfaces;

public class GenericInterfaceTWithGenericImplementationTUImpl<T, U> implements GenericInterfaceTWithGenericImplementationTU<T> {
    private U u;
    private T t;

    @Override
    public T getT() {
        return t;
    }

    @Override
    public void setT(T value) {
        t = value;
    }

    public U getU() {
        return u;
    }

}
