package com.github.nylle.javafixture.testobjects;

public class TestObjectWithNestedGenericInterfaces {

    private ITestGeneric<String, ITestGenericInside<Integer>> testGeneric;

    public ITestGeneric<String, ITestGenericInside<Integer>> getTestGeneric() {
        return testGeneric;
    }
}
