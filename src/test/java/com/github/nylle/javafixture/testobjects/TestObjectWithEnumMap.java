package com.github.nylle.javafixture.testobjects;

import java.util.EnumMap;

public class TestObjectWithEnumMap {
    private String id;
    private EnumMap<TestEnum, String> enums;

    public String getId() {
        return id;
    }

    public EnumMap<TestEnum, String> getEnums() {
        return enums;
    }
}
