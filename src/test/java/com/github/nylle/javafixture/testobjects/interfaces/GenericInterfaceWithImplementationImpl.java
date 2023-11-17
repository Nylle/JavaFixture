package com.github.nylle.javafixture.testobjects.interfaces;

public class GenericInterfaceWithImplementationImpl implements GenericInterfaceWithImplementation<Integer, String> {
    private Integer t;
    private String u;

    @Override
    public Integer getT() {
        return t;
    }

    @Override
    public void setT(Integer value) {
        t = value;
    }

    @Override
    public String getU() {
        return u;
    }
}
