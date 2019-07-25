package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Configuration configuration;
    private final Map<SpecimenType, Object> cache = new HashMap<>();

    public Context(Configuration configuration) {

        if(configuration == null) {
            throw new IllegalArgumentException("configuration: null");
        }

        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public <T> T cached(SpecimenType specimenType, T instance) {
        cache.putIfAbsent(specimenType, instance);
        return (T) cache.get(specimenType);
    }
}

