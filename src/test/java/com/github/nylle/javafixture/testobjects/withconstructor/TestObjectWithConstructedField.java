package com.github.nylle.javafixture.testobjects.withconstructor;


public class TestObjectWithConstructedField {

    private String notSetByConstructor;

    private final int setByConstructor;

    private final TestObjectWithGenericConstructor testObjectWithGenericConstructor;


    public TestObjectWithConstructedField(int setByConstructor, TestObjectWithGenericConstructor testObjectWithGenericConstructor) {
        this.setByConstructor = setByConstructor;
        this.testObjectWithGenericConstructor = testObjectWithGenericConstructor;
    }

    public TestObjectWithGenericConstructor getTestObjectWithGenericConstructor() {
        return testObjectWithGenericConstructor;
    }

    public String getNotSetByConstructor() {
        return notSetByConstructor;
    }

    public int getSetByConstructor() {
        return setByConstructor;
    }
}
