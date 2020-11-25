package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.util.Arrays;
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
        var aClass = (Class<T>) specimen.getClass();

        validate(aClass, customizationContext);

        Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                .filter(field -> !ReflectionHelper.isStatic(field))
                .forEach(field ->
                        ReflectionHelper.setField(
                                field,
                                specimen,
                                customizationContext.getCustomFields()
                                        .getOrDefault(field.getName(), specimenFactory.build(SpecimenType.fromClass(field.getGenericType())).create())));
        return specimen;
    }

    private void validate(Class<T> type, CustomizationContext customizationContext) {
        var declaredFields = Stream.of(type.getDeclaredFields()).map(field -> field.getName()).collect(toList());

        var missingDeclaredField = customizationContext.getCustomFields().entrySet().stream()
                .filter(entry -> !declaredFields.contains(entry.getKey()))
                .findFirst()
                .map(x -> x.getKey());

        if(missingDeclaredField.isPresent()) {
            throw new SpecimenException(format("Cannot set field '%s': Field not found in class '%s'.", missingDeclaredField.get(), type.getName()));
        }
    }
}

