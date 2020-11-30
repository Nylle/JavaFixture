package com.github.nylle.javafixture.testobjects.inheritance;

public class GenericChild<T> extends GenericParent<T> {
    private T childField;
    private String fieldIn3Classes;


    public T getChildField() {
        return childField;
    }

    public String getFieldIn3ClassesChild() {
        return fieldIn3Classes;
    }
}
