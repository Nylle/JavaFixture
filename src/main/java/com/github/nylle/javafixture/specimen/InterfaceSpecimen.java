package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.GenericInvocationHandler;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.Proxy;

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

}

