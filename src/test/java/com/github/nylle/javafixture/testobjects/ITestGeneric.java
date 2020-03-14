package com.github.nylle.javafixture.testobjects;

public interface ITestGeneric<T, U> {
    int publicField = 1;

    T getT();

    U getU();

    void setT(T value);

}
