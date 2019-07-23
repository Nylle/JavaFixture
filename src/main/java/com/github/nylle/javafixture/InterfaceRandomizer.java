package com.github.nylle.javafixture;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;

public class InterfaceRandomizer {

    public static <T> T random(final Class<T> type) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new GenericInvocationHandler<>(type));
    }

    static class GenericInvocationHandler<T> implements InvocationHandler {

        private Map<String, Object> values;

        GenericInvocationHandler(final Class<T> type) {
            values = stream(type.getDeclaredMethods())
                    .filter(x -> x.getReturnType() != void.class)
                    .collect(toMap(x -> x.getName(), x -> Randomizer.random(x.getReturnType())));
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            return values.get(method.getName());
        }
    }

}