package com.github.nylle.javafixture.testobjects.factorymethod;

public class TestObjectWithNonPublicFactoryMethods {
    private String value;

    protected static TestObjectWithNonPublicFactoryMethods protectedStaticMethodReturningABoolean() {
        return new TestObjectWithNonPublicFactoryMethods();
    }

    private static TestObjectWithNonPublicFactoryMethods privateStaticFactoryMethod() {
        return new TestObjectWithNonPublicFactoryMethods();
    }
}
