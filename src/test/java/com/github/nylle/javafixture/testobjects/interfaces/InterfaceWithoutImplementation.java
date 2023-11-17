package com.github.nylle.javafixture.testobjects.interfaces;

import com.github.nylle.javafixture.testobjects.TestObject;

public interface InterfaceWithoutImplementation {
    int publicField = 1;

    TestObject getTestObject();

    void setTestDto(TestObject value);

    String toString();
}
