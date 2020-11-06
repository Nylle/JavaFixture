package com.github.nylle.javafixture.testobjects;

public class TestObjectWithStaticMethods {
    private String value;

    private static Boolean privateStaticMethodReturningABoolean() {
        return false;
    }

    public static Boolean publicStaticMethodReturningABoolean() {
        return false;
    }

    public static void publicStaticMethodReturningAVoid() {

    }

    private static TestObjectWithStaticMethods privateStaticFactoryMethod() {
        return new TestObjectWithStaticMethods();
    }

    public static TestObjectWithStaticMethods publicStaticFactoryMethod() {
        return new TestObjectWithStaticMethods();
    }

}
