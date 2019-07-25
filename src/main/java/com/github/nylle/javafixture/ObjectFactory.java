package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectFactory {

    private final Map<Class<?>, Object> cache = new HashMap<>();

    public <T> T create(final Class<T> type) {
        cache.putIfAbsent(type, newInstance(type));
        return (T) cache.get(type);
    }

    private <T> T newInstance(final Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(type)).newInstance();
        }
    }
}

