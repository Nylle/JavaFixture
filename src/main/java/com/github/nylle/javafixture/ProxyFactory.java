package com.github.nylle.javafixture;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    public <T> T create(final Class<T> type, final Randomizer randomizer) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new GenericInvocationHandler<>(type, randomizer));
    }
}
