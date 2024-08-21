package com.github.nylle.javafixture.testobjects.interfaces;

import com.github.nylle.javafixture.testobjects.TestObject;

import java.util.List;

public interface InterfaceWithDefaultMethod {

    TestObject getTestObject();

    default int getDefaultInt() {
        return 42;
    }

    default void clearsList(List<String> list) {list.clear();}

    void voidMethod(List<String> list);
}
