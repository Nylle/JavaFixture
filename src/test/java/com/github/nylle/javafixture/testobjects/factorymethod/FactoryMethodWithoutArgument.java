package com.github.nylle.javafixture.testobjects.factorymethod;

public class FactoryMethodWithoutArgument {

    private int value;

    private FactoryMethodWithoutArgument(int value) {
        this.value = value;
    }

    public static FactoryMethodWithoutArgument factoryMethod() {
        return new FactoryMethodWithoutArgument(42);
    }

    public int getValue() {
        return value;
    }
}
