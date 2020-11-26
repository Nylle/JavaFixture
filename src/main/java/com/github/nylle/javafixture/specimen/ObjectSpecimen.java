package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.util.stream.Stream;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ObjectSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;

    public ObjectSpecimen(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (type.isPrimitive() || type.isEnum() || type.isBoxed() || type.asClass() == String.class || type.isMap() || type.isCollection() || type.isInterface()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(CustomizationContext customizationContext) {
        if (context.isCached(type)) {
            return (T) context.cached(type);
        }

        if (customizationContext.useRandomConstructor()) {
            return context.cached(type, instanceFactory.construct(type));
        }

        return populate(context.cached(type, instanceFactory.instantiate(type)), customizationContext);
    }

    private T populate(T specimen, CustomizationContext customizationContext) {
        validate(customizationContext);

        type.getDeclaredFields().stream()
                .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                .filter(field -> !field.isStatic())
                .forEach(field ->
                        field.set(
                                specimen,
                                customizationContext.getCustomFields().getOrDefault(
                                        field.getName(),
                                        specimenFactory.build(SpecimenType.fromClass(field.getGenericType())).create())));

        return specimen;
    }

    private void validate(CustomizationContext customizationContext) {
        var declaredFields = type.getDeclaredFields().stream().map(x -> x.getName()).collect(toList());

        var missingDeclaredField = Stream.concat(customizationContext.getCustomFields().keySet().stream(), customizationContext.getIgnoredFields().stream())
                .filter(fieldName -> !declaredFields.contains(fieldName))
                .findFirst();

        if (missingDeclaredField.isPresent()) {
            throw new SpecimenException(format("Cannot customize field '%s': Field not found in class '%s'.", missingDeclaredField.get(), type.getName()));
        }
    }
}

