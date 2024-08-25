package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;

public class InterfaceSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final InstanceFactory instanceFactory;

    public InterfaceSpecimen(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!supportsType(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.instanceFactory = new InstanceFactory(specimenFactory);
    }

    public static <T> boolean supportsType(SpecimenType<T> type) {
        return type.isInterface() && !type.isMap() && !type.isCollection();
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        return (T) instanceFactory.proxy(type);
    }

    public static class Spec implements ISpec {

        @Override
        public <T> boolean supports(SpecimenType<T> type) {
            return supportsType(type);
        }

        @Override
        public <T> ISpecimen<T> create(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {
            return new InterfaceSpecimen<>(type, context, specimenFactory);
        }
    }
}

