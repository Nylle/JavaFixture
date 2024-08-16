package com.github.nylle.javafixture.testobjects.factorymethod;

public class FactoryMethodWithItselfAsArgument {

    private String value;

    private FactoryMethodWithItselfAsArgument(String value) {
        this.value = value;
    }

    public static FactoryMethodWithItselfAsArgument factoryMethod() {
        return new FactoryMethodWithItselfAsArgument(null);
    }

    public static FactoryMethodWithItselfAsArgument factoryMethodWithItselfAsArgument(FactoryMethodWithItselfAsArgument arg) {
        return new FactoryMethodWithItselfAsArgument("this factory method should have been filtered out!");
    }

    public String getValue() {
        return value;
    }
}
