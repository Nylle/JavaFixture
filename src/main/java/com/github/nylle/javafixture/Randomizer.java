package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    private static final int MAX_COLLECTION_SIZE = 10;

    final SpecimenFactory specimenFactory = new SpecimenFactory(new Context(new Configuration()));
    final ProxyFactory proxyFactory = new ProxyFactory();

    public <T> T random(final Class<T> type) {

        if (type.isPrimitive() || type.isEnum() || Reflector.isBoxedOrString(type)) {
            return specimenFactory.build(type).create();
        }

        if (Reflector.isMap(type)) {
            //TODO: non-generic empty map or exception?
            throw new SpecimenException("Unsupported type: " + type);
        }

        if (Reflector.isCollection(type)) {
            //TODO: non-generic empty collection or exception?
            throw new SpecimenException("Unsupported type: " + type);
        }

        if (type.isInterface() && !Reflector.isCollection(type) && !Reflector.isMap(type)) {
            return proxyFactory.create(type, this);
        }

        //TODO: abstract classes: Modifier.isAbstract(type.getModifiers());

        return null;
    }

    int randomLength() {
        return ThreadLocalRandom.current().nextInt(2, MAX_COLLECTION_SIZE + 1);
    }
}

