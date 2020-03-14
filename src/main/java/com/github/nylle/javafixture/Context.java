package com.github.nylle.javafixture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private final Configuration configuration;
    private final Map<Integer, Object> cache = new ConcurrentHashMap<>();

    public Context(Configuration configuration) {

        if(configuration == null) {
            throw new IllegalArgumentException("configuration: null");
        }

        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isCached(FixtureType type) {
        return cache.containsKey(type.hashCode());
    }

    public <T> T cached(FixtureType type, T instance) {
        cache.putIfAbsent(type.hashCode(), instance);
        return (T) cache.get(type.hashCode());
    }

    public <T> T cached(FixtureType type) {
        return (T) cache.get(type.hashCode());
    }
}

