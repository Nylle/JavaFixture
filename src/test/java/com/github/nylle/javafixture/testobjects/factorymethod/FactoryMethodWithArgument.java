package com.github.nylle.javafixture.testobjects.factorymethod;

public class FactoryMethodWithArgument {

    private int value;

    private FactoryMethodWithArgument(int value) {
        this.value = value;
    }

    public static FactoryMethodWithArgument factoryMethod(int value) {
        return new FactoryMethodWithArgument(value);
    }

    public int getValue() {
        return value;
    }
}
