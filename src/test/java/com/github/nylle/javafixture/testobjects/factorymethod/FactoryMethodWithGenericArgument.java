package com.github.nylle.javafixture.testobjects.factorymethod;

public class FactoryMethodWithGenericArgument<T> {

    private T value;

    private FactoryMethodWithGenericArgument(T value) {
        this.value = value;
    }

    public static <T> FactoryMethodWithGenericArgument<T> factoryMethod(T value) {
        return new FactoryMethodWithGenericArgument(42);
    }

    public T getValue() {
        return value;
    }
}
