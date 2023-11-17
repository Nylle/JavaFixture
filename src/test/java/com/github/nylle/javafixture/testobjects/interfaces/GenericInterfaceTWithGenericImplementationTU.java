package com.github.nylle.javafixture.testobjects.interfaces;

public interface GenericInterfaceTWithGenericImplementationTU<T> {
    int publicField = 1;

    T getT();

    void setT(T value);
}
