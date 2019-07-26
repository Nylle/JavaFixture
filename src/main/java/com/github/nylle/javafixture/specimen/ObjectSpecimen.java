package com.github.nylle.javafixture.specimen;

import java.util.Arrays;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

public class ObjectSpecimen<T> implements Specimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public ObjectSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if(type.isPrimitive() || type.isEnum() || Reflector.isBoxedOrString(type) || Reflector.isMap(type) || Reflector.isCollection(type) || type.isInterface()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
    }

    @Override
    public T create() {

        var result = context.cached(SpecimenType.forObject(type), newInstance(type));

        Arrays.stream(type.getDeclaredFields())
                .filter(f -> !Reflector.isStatic(f))
                .forEach(f -> Reflector.setField(f, result, Reflector.isCollection(f.getType())
                        ? specimenFactory.build(f.getType(), Reflector.getGenericType(f.getGenericType(), 0)).create()
                        : specimenFactory.build(f.getType()).create()));

        return result;
    }

    private T newInstance(final Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(type)).newInstance();
        }
    }

}

