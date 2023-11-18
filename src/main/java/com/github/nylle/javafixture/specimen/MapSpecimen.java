package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

public class MapSpecimen<T, K, V> implements ISpecimen<T> {
    private final SpecimenType<T> type;
    private final Context context;
    private ISpecimen<K> keySpecimen;
    private ISpecimen<V> valueSpecimen;

    public MapSpecimen(final SpecimenType<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isMap()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;

        if (type.isParameterized()) {
            this.keySpecimen = specimenFactory.build(type.getGenericTypeArgument(0));
            this.valueSpecimen = specimenFactory.build(type.getGenericTypeArgument(1));
        }
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        Map<K, V> map = context.cached(type, type.isInterface() ? createFromInterfaceType(type.asClass()) : createFromConcreteType(type));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> keySpecimen != null && valueSpecimen != null)
                .forEach(x -> map.put(keySpecimen.create(customizationContext, new Annotation[0]), valueSpecimen.create(customizationContext, new Annotation[0])));

        return (T) map;
    }

    private Map<K, V> createFromConcreteType(final SpecimenType<T> type) {
        if (type.asClass().equals(EnumMap.class)) {
            return (Map<K, V>) new EnumMap(type.getGenericTypeArgument(0).asClass());
        }

        try {
            return (Map<K, V>) type.asClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new SpecimenException("Unable to create map of type " + type.getName(), e);
        }
    }

    private Map<K, V> createFromInterfaceType(final Class<T> type) {

        if (ConcurrentNavigableMap.class.isAssignableFrom(type)) {
            return new ConcurrentSkipListMap<>();
        }

        if (ConcurrentMap.class.isAssignableFrom(type)) {
            return new ConcurrentHashMap<>();
        }

        if (NavigableMap.class.isAssignableFrom(type)) {
            return new TreeMap<>();
        }

        if (SortedMap.class.isAssignableFrom(type)) {
            return new TreeMap<>();
        }

        if (Map.class.isAssignableFrom(type)) {
            return new HashMap<>();
        }

        throw new SpecimenException("Unsupported type: " + type);
    }

}
