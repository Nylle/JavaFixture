package com.github.nylle.javafixture.testobjects;

public class TestObjectGeneric<T, U> {
    private T t;
    private U u;

    public T getT() {
        return t;
    }

    public void setT(final T t) {
        this.t = t;
    }

    public U getU() {
        return u;
    }

    public void setU(final U u) {
        this.u = u;
    }
}

