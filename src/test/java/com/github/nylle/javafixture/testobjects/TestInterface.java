package com.github.nylle.javafixture.testobjects;

public interface TestInterface {
    int publicField = 1;

    TestPrimitive getTestDto();

    void setTestDto(TestPrimitive value);

    String toString();
}
