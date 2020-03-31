package com.github.nylle.javafixture.testobjects;

import java.util.EnumSet;

public class TestObjectWithEnumSet {
    private String id;
    private EnumSet<TestEnum> enums;

    public String getId() {
        return id;
    }

    public EnumSet<TestEnum> getEnums() {
        return enums;
    }
}
