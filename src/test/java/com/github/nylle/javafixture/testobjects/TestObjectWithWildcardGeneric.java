package com.github.nylle.javafixture.testobjects;

import java.util.List;

public class TestObjectWithWildcardGeneric {
    private final List<?> integers;

    public TestObjectWithWildcardGeneric(final List<Integer> integers) {
        this.integers = integers;
    }

    public List<?> getIntegers() {
        return integers;
    }
}
