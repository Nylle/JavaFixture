package com.github.nylle.javafixture.testobjects.interfaces;

public interface GenericInterfaceTUWithGenericImplementationT<T, U> {
    int publicField = 1;

    T getT();

    void setT(T value);

    U getU();
}
