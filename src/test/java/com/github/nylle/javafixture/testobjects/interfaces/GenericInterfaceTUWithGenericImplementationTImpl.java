package com.github.nylle.javafixture.testobjects.interfaces;

public class GenericInterfaceTUWithGenericImplementationTImpl<T> implements GenericInterfaceTUWithGenericImplementationT<T, String> {
    private T t;
    private String u;

    @Override
    public T getT() {
        return t;
    }

    @Override
    public void setT(T value) {
        t = value;
    }

    @Override
    public String getU() {
        return u;
    }
}
