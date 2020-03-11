package com.github.nylle.javafixture.testobjects;

import java.util.Optional;

public class TestObjectWithNestedGenerics {

    private Optional<TestObjectGeneric<String, Integer>> optionalGeneric;

    private TestObjectGeneric<String, Optional<Integer>> genericOptional;

    public Optional<TestObjectGeneric<String, Integer>> getOptionalGeneric() {
        return optionalGeneric;
    }

    public TestObjectGeneric<String, Optional<Integer>> getGenericOptional() {
        return genericOptional;
    }
}
