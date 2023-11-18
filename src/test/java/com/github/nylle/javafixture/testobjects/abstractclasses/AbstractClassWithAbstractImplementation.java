package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public abstract class AbstractClassWithAbstractImplementation {
    int publicField = 1;

    abstract TestObject getTestObject();

    abstract void setTestDto(TestObject value);
}
