package com.github.nylle.javafixture.testobjects.abstractclasses;

import com.github.nylle.javafixture.testobjects.TestObject;

import java.io.IOException;
import java.util.List;

public abstract class AbstractClassWithConcreteMethod {
    abstract public TestObject getTestObject();

    public int getDefaultInt() {
        return 42;
    }

    public void methodWithSideEffect(List<String> list) {
        list.clear();
    }

    public void methodThatThrowsException() throws IOException {
        throw new IOException("the signature said it");
    }

    abstract public void abstractMethodWithoutSideEffect(List<String> list);
}
