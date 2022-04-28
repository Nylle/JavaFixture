package com.github.nylle.javafixture.testobjects.factorymethod;

public class GenericClassWithFactoryMethodWithoutArgument<T> {

    private T value;

    private GenericClassWithFactoryMethodWithoutArgument(T value) {
        this.value = value;
    }

    public static <T> GenericClassWithFactoryMethodWithoutArgument<T> factoryMethod() {
        return new GenericClassWithFactoryMethodWithoutArgument(42);
    }

    public T getValue() {
        return value;
    }
}
