package com.github.nylle.javafixture.testobjects.inheritance;

public class GenericParent<T> extends GenericBase<T> {
    private T parentField;
    private Double fieldIn2Classes;
    private Double fieldIn3Classes;

    public T getParentField() {
        return parentField;
    }

    public Double getFieldIn2ClassesParent() {
        return fieldIn2Classes;
    }

    public Double getFieldIn3ClassesParent() {
        return fieldIn3Classes;
    }
}
