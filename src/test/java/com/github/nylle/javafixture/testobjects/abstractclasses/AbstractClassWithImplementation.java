package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public abstract class AbstractClassWithImplementation {
    int publicField = 1;

    public abstract TestObject getTestObject();

    public abstract void setTestDto(TestObject value);
}
