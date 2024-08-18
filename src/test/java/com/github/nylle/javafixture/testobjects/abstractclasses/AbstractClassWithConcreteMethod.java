package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

public abstract class AbstractClassWithConcreteMethod {
    abstract public TestObject getTestObject();

    public int getDefaultInt() {
        return 42;
    }
}
