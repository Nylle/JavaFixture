package com.github.nylle.javafixture.testobjects.interfaces;

import com.github.nylle.javafixture.testobjects.TestObject;

public class InterfaceWithImplementationImpl implements InterfaceWithImplementation {
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
