package com.github.nylle.javafixture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private final Configuration configuration;
    private final Map<SpecimenType<?>, Object> cache;

    public Context(Configuration configuration) {

        if (configuration == null) {
            throw new IllegalArgumentException("configuration: null");
        }

        this.configuration = configuration;
        this.cache = new ConcurrentHashMap<>();
    }

    public Context(Configuration configuration, Map<SpecimenType<?>, Object> predefinedInstances) {

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
        return cache.containsKey(type);
    }

    public <T> T overwrite(SpecimenType<?> type, T instance) {
        cache.put(type, instance);
        return (T) cache.get(type);
    }

    public <T> T cached(SpecimenType<?> type, T instance) {
        cache.putIfAbsent(type, instance);
        return (T) cache.get(type);
    }

    public <T> T cached(SpecimenType<T> type) {
        return (T) cache.get(type);
    }

    public <T> T preDefined(SpecimenType<T> type, T instance) {
        return cache.containsKey(type) ? (T) cache.get(type) : instance;
    }

}

