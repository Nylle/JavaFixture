package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;

public class GenericSpecimen<T, G> implements ISpecimen<T> {

    private final Class<T> type;
    private final Class<G> genericType;

    public GenericSpecimen(final Class<T> type, final Class<G> genericType) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if(genericType == null) {
            throw new IllegalArgumentException("genericType: null");
        }

        if (!type.equals(Class.class) && !ReflectionHelper.isParameterizedType(type)) {
            throw new IllegalArgumentException("type is not generic: " + type.getName());
        }

        this.type = type;
        this.genericType = genericType;
    }

    @Override
    public T create() {
        if (type.equals(Class.class)) {
            return (T) genericType;
        }

        throw new SpecimenException("Unsupported generic type: "+ type);
    }
}

