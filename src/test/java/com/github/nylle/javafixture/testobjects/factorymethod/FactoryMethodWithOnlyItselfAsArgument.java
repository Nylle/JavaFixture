package com.github.nylle.javafixture.testobjects.factorymethod;

public class FactoryMethodWithOnlyItselfAsArgument {

    private FactoryMethodWithOnlyItselfAsArgument() {
    }

    public static FactoryMethodWithOnlyItselfAsArgument factoryMethodWithItselfAsArgument(FactoryMethodWithOnlyItselfAsArgument arg) {
        return new FactoryMethodWithOnlyItselfAsArgument();
    }
}
