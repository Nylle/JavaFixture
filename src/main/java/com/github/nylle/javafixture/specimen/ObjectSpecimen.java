package com.github.nylle.javafixture.specimen;

import java.util.Arrays;

import com.github.nylle.javafixture.SpecimenException;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

public class ObjectSpecimen<T> implements Specimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;

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

        if(type.isPrimitive() || type.isEnum() || ReflectionHelper.isBoxedOrString(type) || ReflectionHelper.isMap(type) || ReflectionHelper.isCollection(type) || type.isInterface()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forObject(type);
    }

    @Override
    public T create() {

        if(context.isCached(specimenType)){
            return (T) context.cached(specimenType);
        }

        var result = context.cached(specimenType, newInstance(type));

        Arrays.stream(type.getDeclaredFields())
                .filter(x -> !ReflectionHelper.isStatic(x))
                .forEach(x -> ReflectionHelper.setField(x, result, ReflectionHelper.isParameterizedType(x.getGenericType())
                        ? specimenFactory.build(x.getType(), x.getGenericType()).create()
                        : specimenFactory.build(x.getType()).create()));

        return result;
    }

    private T newInstance(final Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            try {
                return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(type)).newInstance();
            }
            catch(Exception f) {
                throw new SpecimenException("Unable to create object of type " + type.getName(), f);
            }
        }
    }

}

