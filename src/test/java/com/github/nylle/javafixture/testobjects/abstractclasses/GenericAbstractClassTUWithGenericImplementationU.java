package com.github.nylle.javafixture.testobjects.abstractclasses;

public abstract class GenericAbstractClassTUWithGenericImplementationU<T, U> {
    int publicField = 1;

    public abstract T getT();

    public abstract void setT(T value);

    public abstract U getU();
}
