package com.github.nylle.javafixture.testobjects;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class TestWildCardType implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    public TestWildCardType(Type[] upperBounds, Type[] lowerBounds) {
        this.upperBounds = upperBounds;
        this.lowerBounds = lowerBounds;
    }

    @Override
    public Type[] getUpperBounds() {
        return upperBounds;
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBounds;
    }
}
