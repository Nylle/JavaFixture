package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public class AbstractClassWithImplementationImpl extends AbstractClassWithImplementation {
    private TestObject testObject;

    @Override
    public TestObject getTestObject() {
        return testObject;
    }

    @Override
    public void setTestDto(TestObject value) {
        testObject = value;
    }
}
