package com.github.nylle.javafixture.testobjects.inheritance;

public class GenericBase<T> {
    private T baseField;
    private Integer fieldIn2Classes;
    private Integer fieldIn3Classes;

    public T getBaseField() {
        return baseField;
    }

    public Integer getFieldIn2ClassesBase() {
        return fieldIn2Classes;
    }

    public Integer getFieldIn3ClassesBase() {
        return fieldIn3Classes;
    }
}
