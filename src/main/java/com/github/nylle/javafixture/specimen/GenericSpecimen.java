package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
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
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(CustomizationContext customizationContext) {
        if (type.asClass().equals(Class.class)) {
            return (T) specimens.entrySet().stream().findFirst().get().getValue().create().getClass();
        }

        if (context.isCached(type)) {
            return (T) context.cached(type);
        }

        if (type.isInterface()) {
            return (T) context.cached(type, instanceFactory.proxy(type, specimens));
        }

        if (customizationContext.useRandomConstructor()) {
            return context.cached(type, instanceFactory.construct(type));
        }

        var result = context.cached(type, instanceFactory.instantiate(type));

        validateCustomization(type.asClass(), customizationContext);

        stream(type.asClass().getDeclaredFields())
                .filter(x -> !customizationContext.getIgnoredFields().contains(x.getName()))
                .filter(field -> !ReflectionHelper.isStatic(field))
                .forEach(field -> customize(field, result, customizationContext));

        return result;
    }

    private void customize(Field field, T result, CustomizationContext customizationContext) {
        if (customizationContext.getCustomFields().containsKey(field.getName())) {
            ReflectionHelper.setField(field, result, customizationContext.getCustomFields().get(field.getName()));
        } else {
            ReflectionHelper.setField(field, result, specimens.getOrDefault(field.getGenericType().getTypeName(), specimenFactory.build(SpecimenType.fromClass(field.getType()))).create());
        }
    }

    private void validateCustomization(Class<T> type, CustomizationContext customizationContext) {
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

