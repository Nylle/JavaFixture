package com.github.nylle.javafixture.testobjects.abstractclasses;

public class GenericAbstractClassTUWithGenericImplementationUImpl<U> extends GenericAbstractClassTUWithGenericImplementationU<String, U> {
    private String t;
    private U u;

    @Override
    public String getT() {
        return t;
    }

    @Override
    public void setT(String value) {
        t = value;
    }

    @Override
    public U getU() {
        return u;
    }
}
