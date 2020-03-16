package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
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

import static com.github.nylle.javafixture.CustomizationContext.noContext;

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

        if(type.isParameterized()) {
            this.keySpecimen = specimenFactory.build(SpecimenType.fromClass(type.getGenericTypeArgument(0)));
            this.valueSpecimen = specimenFactory.build(SpecimenType.fromClass(type.getGenericTypeArgument(1)));
        }
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if (context.isCached(type)) {
            return (T) context.cached(type);
        }

        Map<K, V> map = context.cached(type, type.isInterface() ? createFromInterfaceType(type.asClass()) : createFromConcreteType(type.asClass()));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> keySpecimen != null && valueSpecimen != null)
                .forEach(x -> map.put(keySpecimen.create(), valueSpecimen.create()));

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
