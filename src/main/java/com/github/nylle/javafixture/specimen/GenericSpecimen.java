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
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class GenericSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;
    private final Map<String, ISpecimen<?>> specimens;

    public GenericSpecimen(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isParameterized()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (type.isCollection() || type.isMap()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);

        this.specimens = IntStream.range(0, type.getGenericTypeArguments().length)
                .boxed()
                .collect(toMap(
                        i -> type.getTypeParameterName(i),
                        i -> specimenFactory.build(SpecimenType.fromClass(type.getGenericTypeArgument(i)))));
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        if (type.asClass().equals(Class.class)) {
            return (T) specimens.entrySet().stream().findFirst().get().getValue().create(customizationContext, new Annotation[0]).getClass();
        }

        if (context.isCached(type)) {
            return context.cached(type);
        }

        if (type.isInterface()) {
            return (T) context.cached(type, instanceFactory.proxy(type, specimens));
        }

        if (customizationContext.useRandomConstructor()) {
            return context.cached(type, instanceFactory.construct(type, customizationContext));
        }

        return populate(customizationContext);
    }

    private T populate(CustomizationContext customizationContext) {
        var result = context.cached(type, instanceFactory.instantiate(type));
        var reflector = new Reflector<>(result)
                .validateCustomization(customizationContext, type);
        try {
            reflector.getDeclaredFields()
                    .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                    .forEach(field -> reflector.setField(field,
                            customizationContext.getCustomFields().getOrDefault(
                                    field.getName(),
                                    specimens.getOrDefault(
                                            field.getGenericType().getTypeName(),
                                            specimenFactory.build(SpecimenType.fromClass(field.getType()))).create(new CustomizationContext(List.of(), Map.of()), new Annotation[0]))));
        } catch (SpecimenException ex) {
            return context.overwrite(type, instanceFactory.construct(type, customizationContext));
        }
        return result;
    }
}

