package com.github.nylle.javafixture.testobjects.abstractclasses;

public abstract class GenericAbstractClassTUWithGenericImplementationU<T, U> {
    int publicField = 1;

    abstract T getT();

    abstract void setT(T value);

    abstract U getU();
}
