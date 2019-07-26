package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class InterfaceSpecimen<T> implements Specimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public InterfaceSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if(!type.isInterface() || Reflector.isMap(type) || Reflector.isCollection(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
    }

    @Override
    public T create() {
        return context.cached(SpecimenType.forObject(type), newProxy(type));
    }

    private T newProxy(Class<T> type) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new GenericInvocationHandler<>(type, specimenFactory));
    }

    class GenericInvocationHandler<U> implements InvocationHandler {

        private Map<String, Object> values;

        GenericInvocationHandler(final Class<U> type, final SpecimenFactory specimenFactory) {
            values = stream(type.getDeclaredMethods())
                    .filter(x -> x.getReturnType() != void.class)
                    .collect(toMap(x -> x.getName(), x -> specimenFactory.build(x.getReturnType()).create()));
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            return values.get(method.getName());
        }
    }


}

