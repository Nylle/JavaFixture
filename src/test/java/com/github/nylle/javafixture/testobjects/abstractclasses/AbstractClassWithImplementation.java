package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public abstract class AbstractClassWithImplementation {
    int publicField = 1;

    abstract TestObject getTestObject();

    abstract void setTestDto(TestObject value);
}
