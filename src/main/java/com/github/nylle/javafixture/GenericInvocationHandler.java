package com.github.nylle.javafixture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class GenericInvocationHandler<T> implements InvocationHandler {

    private Map<String, Object> values;

    public GenericInvocationHandler(final Class<T> type, final Randomizer randomizer) {
        values = stream(type.getDeclaredMethods())
                .filter(x -> x.getReturnType() != void.class)
                .collect(toMap(x -> x.getName(), x -> randomizer.random(x.getReturnType())));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        return values.get(method.getName());
    }
}
