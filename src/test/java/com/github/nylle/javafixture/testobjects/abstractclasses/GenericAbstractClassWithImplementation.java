package com.github.nylle.javafixture.testobjects.abstractclasses;

public abstract class GenericAbstractClassWithImplementation<T, U> {
    int publicField = 1;

    abstract T getT();

    abstract void setT(T value);

    abstract U getU();
}
