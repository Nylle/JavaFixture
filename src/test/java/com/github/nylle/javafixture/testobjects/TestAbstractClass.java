package com.github.nylle.javafixture.testobjects;

public abstract class TestAbstractClass {

    private String string;

    public String getString() {
        return string;
    }

    public abstract String abstractMethod();

    private String privateMethod() {
        return "private";
    }
}
