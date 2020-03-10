package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.IntStream;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static com.github.nylle.javafixture.ReflectionHelper.newInstance;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class GenericSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;
    private final Map<String, ISpecimen<?>> specimens;


    public GenericSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory, final ISpecimen<?>... specimens) throws IllegalArgumentException {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (specimens == null) {
            throw new IllegalArgumentException("specimens: null");
        }

        if (specimens.length == 0) {
            throw new IllegalArgumentException("no specimens provided");
        }

        if (type.getTypeParameters() == null || type.getTypeParameters().length == 0) {
            throw new IllegalArgumentException(format("type does not appear to be generic: %s", type));
        }

        if (type.getTypeParameters().length != specimens.length) {
            throw new IllegalArgumentException(format("number of type parameters (%d) does not match number of provided specimens: %d", type.getTypeParameters().length, specimens.length));
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forGeneric(type, stream(specimens).map(s -> s.getClass()).toArray(size -> new Class<?>[size]));
        this.specimens = IntStream.range(0, type.getTypeParameters().length).boxed().collect(toMap(i -> type.getTypeParameters()[i].getName(), i -> specimens[i]));
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if (type.equals(Class.class)) {
            return (T) specimens.entrySet().stream().findFirst().get().getValue().create().getClass();
        }

        if (context.isCached(specimenType)) {
            return (T) context.cached(specimenType);
        }

        var result = context.cached(specimenType, newInstance(type));

        stream(type.getDeclaredFields())
                .filter(field -> !ReflectionHelper.isStatic(field))
                .forEach(field -> ReflectionHelper.setField(field, result, specimens.getOrDefault(field.getGenericType().getTypeName(), specimenFactory.build(field.getType())).create()));
                .filter(x -> !customizationContext.getIgnoredFields().contains(x.getName()))
                .filter(x -> !ReflectionHelper.isStatic(x))
                .forEach(x -> customize(x, result, customizationContext));

        return result;
    }

    private void customize(Field x, T result, CustomizationContext customizationContext) {
        if(customizationContext.getCustomFields().containsKey(x.getName())) {
            ReflectionHelper.setField(x, result, customizationContext.getCustomFields().get(x.getName()));
        } else {
            ReflectionHelper.setField(x, result, ReflectionHelper.isParameterizedType(x.getGenericType())
                    ? specimenFactory.build(resolveType(x), x.getGenericType()).create()
                    : specimenFactory.build(resolveType(x)).create());
        }
    }

}

