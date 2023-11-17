package com.github.nylle.javafixture.testobjects.interfaces;

import com.github.nylle.javafixture.testobjects.TestObject;

public interface InterfaceWithImplementation {
    int publicField = 1;

    TestObject getTestObject();

    void setTestDto(TestObject value);

    String toString();
}
