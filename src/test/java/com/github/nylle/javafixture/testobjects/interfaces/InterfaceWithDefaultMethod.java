package com.github.nylle.javafixture.testobjects.interfaces;

import com.github.nylle.javafixture.testobjects.TestObject;

public interface InterfaceWithDefaultMethod {

    TestObject getTestObject();

    default int getDefaultInt() {
        return 42;
    }
}
