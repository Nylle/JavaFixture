package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    private static final int MAX_COLLECTION_SIZE = 10;

    final CollectionFactory collectionFactory = new CollectionFactory();
    final SpecimenFactory specimenFactory = new SpecimenFactory(new Context(new Configuration()));
    final ProxyFactory proxyFactory = new ProxyFactory();
    final ObjectFactory objectFactory = new ObjectFactory();

    public <T> T random(final Class<T> type) {

        if(type.isPrimitive() || type.isEnum() || Reflector.isBoxedOrString(type)) {
            return specimenFactory.build(type).create();
        }

        if(Reflector.isMap(type)) {
            //TODO: non-generic empty map or exception?
            throw new SpecimenException("Unsupported type: "+ type);
        }

        if(Reflector.isCollection(type)) {
            //TODO: non-generic empty collection or exception?
            throw new SpecimenException("Unsupported type: "+ type);
        }

        if(type.isInterface() && !Reflector.isCollection(type) && !Reflector.isMap(type)) {
            return proxyFactory.create(type, this);
        }

        try {
            //TODO: abstract classes: Modifier.isAbstract(type.getModifiers());

            final T result = objectFactory.create(type);

            for (Field field : type.getDeclaredFields()) {
                if(field.getType().equals(type)) {
                    continue; // recursion!
                }

                if (Reflector.isStatic(field)) {
                    continue;
                }

                try {
                    field.setAccessible(true);

                    if(Reflector.isCollection(field.getType())) {
                        field.set(result, collectionFactory.create(field.getType(), field.getGenericType(), this));
                    } else {
                        field.set(result, random(field.getType()));
                    }
                } catch (SecurityException e) {
                    throw new SpecimenException("Unable to access field " + field.getName() + " on object of type " + type.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new SpecimenException("Unable to set field " + field.getName() + " on object of type " + type.getName(), e);
                }
            }
            return result;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new SpecimenException("Unable to create object of type " + type.getName(), e);
        }
    }

    int randomLength() {
        return ThreadLocalRandom.current().nextInt(2, MAX_COLLECTION_SIZE + 1);
    }
}

