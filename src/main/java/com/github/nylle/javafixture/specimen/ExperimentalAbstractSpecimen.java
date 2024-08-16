package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ExperimentalAbstractSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;
    private final List<SpecimenType<? extends T>> derivedTypes;

    public ExperimentalAbstractSpecimen(SpecimenType<T> type, List<SpecimenType<? extends T>> derivedTypes, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (derivedTypes == null) {
            throw new IllegalArgumentException("derivedTypes: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (isNotAbstract(type) || isCollection(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);
        this.derivedTypes = derivedTypes;
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        return context.cached(type, shuffledStream(derivedTypes)
                .map(derivedType -> specimenFactory.build(derivedType))
                .flatMap(derivedSpecimen -> tryCreate(derivedSpecimen, customizationContext, annotations).stream())
                .findFirst()
                .orElseGet(() -> proxy(customizationContext)));
    }

    private <R extends T> R proxy(CustomizationContext customizationContext) {
        try {
            return (R) instanceFactory.proxy(type);
        } catch (SpecimenException ex) {
            if (type.isAbstract()) {
                return (R) instanceFactory.manufacture(type, customizationContext);
            }
            throw ex;
        }
    }

    private static <T> Optional<T> tryCreate(ISpecimen<T> specimen, CustomizationContext customizationContext, Annotation[] annotations) {
        try {
            return Optional.of(specimen.create(customizationContext, annotations));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private static <T> boolean isNotAbstract(SpecimenType<T> type) {
        return !(type.isAbstract() || type.isInterface());
    }

    private static <T> boolean isCollection(SpecimenType<T> type) {
        return type.isMap() || type.isCollection();
    }

    private static <T> Stream<SpecimenType<? extends T>> shuffledStream(List<SpecimenType<? extends T>> list) {
        var copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.stream();
    }
}

