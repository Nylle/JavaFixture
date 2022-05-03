package com.github.nylle.javafixture.testobjects.withconstructor;

import java.util.List;


public class TestObjectWithoutDefaultConstructor {
    private List<String> strings;

    public TestObjectWithoutDefaultConstructor(List<String> strings) {
        this.strings = strings;
    }

    public List<String> getStrings() {
        return strings;
    }
}
