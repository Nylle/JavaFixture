package com.github.nylle.javafixture.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.lang.String.format;

abstract class TypeCapture<T> {
    final Type capture() {
        Type superclass = getClass().getGenericSuperclass();

        if(!(superclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException(format("%s doesn't seem to have been instantiated with generic type arguments", superclass));
        }

        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
