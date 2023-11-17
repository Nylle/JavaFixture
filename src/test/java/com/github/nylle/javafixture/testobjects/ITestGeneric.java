package com.github.nylle.javafixture.testobjects;

public interface ITestGeneric<T, U> {
    int publicField = 1;

    T getT();

    void setT(T value);

    U getU();
}
