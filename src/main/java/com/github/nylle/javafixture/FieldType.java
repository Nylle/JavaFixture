package com.github.nylle.javafixture;

import java.lang.reflect.Type;

public class FieldType {

    private final Class<?> type;
    private final Type genericType;

    public FieldType(final Class<?> type, final Type genericType) {
        this.type = type;
        this.genericType = genericType;
    }

    public Class<?> getType() {
        return type;
    }

    public Type getGenericType() {
        return genericType;
    }
}
