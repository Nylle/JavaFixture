package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class AbstractSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;

    public AbstractSpecimen(final SpecimenType<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isAbstract() || type.isMap() || type.isCollection()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        try {
            var result = (T) context.cached(type, instanceFactory.proxy(type));
            var reflector = new Reflector<>(result, type.asClass()).validateCustomization(customizationContext, type);
            reflector.getDeclaredFields()
                    .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                    .forEach(field -> reflector.setField(field,
                            customizationContext.getCustomFields().getOrDefault(
                                    field.getName(),
                                    Map.<String, ISpecimen<?>>of().getOrDefault(
                                            field.getGenericType().getTypeName(),
                                            specimenFactory.build(SpecimenType.fromClass(field.getGenericType()))).create(new CustomizationContext(List.of(), Map.of(), false), field.getAnnotations()))));
            return result;
        } catch(SpecimenException ignored) {
            return context.cached(type, instanceFactory.manufacture(type, customizationContext));
        }
    }
}

