package com.github.nylle.javafixture.testobjects.withconstructor;


public class TestObjectWithConstructedField {

    private String notSetByConstructor;

    private final TestObjectWithGenericConstructor testObjectWithGenericConstructor;


    public TestObjectWithConstructedField(TestObjectWithGenericConstructor testObjectWithGenericConstructor) {
        this.testObjectWithGenericConstructor = testObjectWithGenericConstructor;
    }

    public TestObjectWithGenericConstructor getTestObjectWithGenericConstructor() {
        return testObjectWithGenericConstructor;
    }

    public String getNotSetByConstructor() {
        return notSetByConstructor;
    }
}
