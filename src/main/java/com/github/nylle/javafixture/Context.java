package com.github.nylle.javafixture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private final Configuration configuration;
    private final Map<Integer, Object> cache;

    public Context(Configuration configuration) {

        if (configuration == null) {
            throw new IllegalArgumentException("configuration: null");
        }

        this.configuration = configuration;
        this.cache = new ConcurrentHashMap<>();
    }

    public Context(Configuration configuration, Map<Integer, Object> predefinedInstances) {

        if (configuration == null) {
            throw new IllegalArgumentException("configuration: null");
        }

        this.configuration = configuration;
        this.cache = new ConcurrentHashMap<>(predefinedInstances);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isCached(SpecimenType<?> type) {
        return cache.containsKey(type.hashCode());
    }

    public <T> T overwrite(SpecimenType<?> type, T instance) {
        cache.put(type.hashCode(), instance);
        return (T) cache.get(type.hashCode());
    }

    public <T> T cached(SpecimenType<?> type, T instance) {
        cache.putIfAbsent(type.hashCode(), instance);
        return (T) cache.get(type.hashCode());
    }

    public <T> T cached(SpecimenType<T> type) {
        return (T) cache.get(type.hashCode());
    }

    public <T> T preDefined(SpecimenType<T> type, T instance) {
        return cache.containsKey(type.hashCode()) ? (T) cache.get(type.hashCode()) : instance;
    }

}

