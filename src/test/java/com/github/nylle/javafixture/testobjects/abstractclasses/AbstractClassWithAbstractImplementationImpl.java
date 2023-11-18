package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public abstract class AbstractClassWithAbstractImplementationImpl extends AbstractClassWithAbstractImplementation {

    abstract TestObject getTestObject();

    abstract void setTestDto(TestObject value);
}
