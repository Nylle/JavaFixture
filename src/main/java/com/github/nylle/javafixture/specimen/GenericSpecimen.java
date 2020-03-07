package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.IntStream;

import static com.github.nylle.javafixture.ReflectionHelper.newInstance;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class GenericSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;
    private final Map<String, Class<?>> genericTypes;


    public GenericSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory, final Class<?>... genericTypes) throws IllegalArgumentException {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (genericTypes == null) {
            throw new IllegalArgumentException("genericTypes: null");
        }

        if (genericTypes.length == 0) {
            throw new IllegalArgumentException("no genericTypes provided");
        }

        if (type.getTypeParameters() == null || type.getTypeParameters().length == 0) {
            throw new IllegalArgumentException(format("type does not appear to be generic: %s", type));
        }

        if (type.getTypeParameters().length != genericTypes.length) {
            throw new IllegalArgumentException(format("number of type parameters (%d) does not match number of provided generic types: %d", type.getTypeParameters().length, genericTypes.length));
        }


        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forGeneric(type, genericTypes);
        this.genericTypes = IntStream.range(0, type.getTypeParameters().length)
                .boxed()
                .collect(toMap(i -> type.getTypeParameters()[i].getName(), i -> genericTypes[i]));
    }

    @Override
    public T create() {
        if (type.equals(Class.class)) {
            return (T) genericTypes.entrySet().stream().findFirst().get().getValue();
        }

        if (context.isCached(specimenType)) {
            return (T) context.cached(specimenType);
        }

        var result = context.cached(specimenType, newInstance(type));

        stream(type.getDeclaredFields())
                .filter(x -> !ReflectionHelper.isStatic(x))
                .forEach(x -> ReflectionHelper.setField(x, result, ReflectionHelper.isParameterizedType(x.getGenericType())
                        ? specimenFactory.build(resolveType(x), x.getGenericType()).create()
                        : specimenFactory.build(resolveType(x)).create()));

        return result;
    }

    private Class<?> resolveType(final Field field) {
        return genericTypes.getOrDefault(field.getGenericType().getTypeName(), field.getType());
    }
}

