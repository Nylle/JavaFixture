package com.github.nylle.javafixture.testobjects;

public interface TestInterface {
    int publicField = 1;

    TestObject getTestObject();

    void setTestDto(TestObject value);

    String toString();
}
