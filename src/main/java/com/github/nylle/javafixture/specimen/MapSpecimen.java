package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.InvocationTargetException;
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
    private final Class<T> type;
    private final Class<K> genericKeyType;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;
    private ISpecimen<V> valueSpecimen;

    public MapSpecimen(final Class<T> type, final Class<K> genericKeyType, final Class<V> genericValueType, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (genericKeyType == null && genericValueType != null) {
            throw new IllegalArgumentException("genericKeyType: null");
        }
        if (genericValueType == null && genericKeyType != null) {
            throw new IllegalArgumentException("genericValueType: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!ReflectionHelper.isMap(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.genericKeyType = genericKeyType;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forMap(type, genericKeyType, genericValueType);
        this.valueSpecimen = genericValueType == null ? null : specimenFactory.build(genericValueType);
    }

    public MapSpecimen(final Class<T> type, final Class<K> genericKeyType, Class<V> genericValueType, final Context context, final SpecimenFactory specimenFactory, final ISpecimen<V> specimen) {

        this(type, genericKeyType, genericValueType, context, specimenFactory);
        this.valueSpecimen = specimen;
    }

    @Override
    public T create() {
        if (context.isCached(specimenType)) {
            return (T) context.cached(specimenType);
        }

        Map<K, V> map = context.cached(specimenType, type.isInterface() ? createFromInterfaceType(type) : createFromConcreteType(type));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> genericKeyType != null && valueSpecimen != null)
                .forEach(x -> map.put(
                        specimenFactory.build(genericKeyType).create(),
                       valueSpecimen.create()));

        return (T) map;
    }

    private Map<K, V> createFromConcreteType(final Class<T> type) {
        try {
            return (Map<K, V>) type.getDeclaredConstructor().newInstance();
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
