package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class Randomizer {
    private static final int MAX_COLLECTION_SIZE = 10;

    final Random random = new Random();
    final CollectionFactory collectionFactory = new CollectionFactory();

    public <T> T random(final Class<T> type) {

        if(type.isPrimitive() || Reflector.isBoxedOrString(type)) {

            if (type.equals(String.class)) {
                return (T) UUID.randomUUID().toString();
            }

            if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                return (T) Boolean.valueOf(random.nextBoolean());
            }

            if (type.equals(Byte.class) || type.equals(byte.class)) {
                return (T) Byte.valueOf(UUID.randomUUID().toString().getBytes(Charset.defaultCharset())[0]);
            }

            if (type.equals(Short.class) || type.equals(short.class)) {
                return (T) Short.valueOf((short)random.nextInt(Short.MAX_VALUE + 1));
            }

            if (type.equals(Integer.class) || type.equals(int.class)) {
                return (T) Integer.valueOf(random.nextInt());
            }

            if (type.equals(Long.class) || type.equals(long.class)) {
                return (T) Long.valueOf(random.nextLong());
            }

            if (type.equals(Float.class) || type.equals(float.class)) {
                return (T) Float.valueOf(random.nextFloat());
            }

            if (type.equals(Double.class) || type.equals(double.class)) {
                return (T) Double.valueOf(random.nextDouble());
            }

            throw new RandomizerException("Unsupported type: "+ type);
        }

        if(type.isEnum()) {
            return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
        }

        if(Reflector.isMap(type)) {
            //TODO: non-generic map or exception?
            throw new RandomizerException("Unsupported type: "+ type);
        }

        if(Reflector.isCollection(type)) {
            //TODO: non-generic collection or exception?
            throw new RandomizerException("Unsupported type: "+ type);
        }

        if(type.isInterface()) {
            return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new GenericInvocationHandler<>(type, this));
        }

        try {
            //TODO: abstract classes: Modifier.isAbstract(type.getModifiers());

            final T result = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                try {
                    if(Reflector.isCollection(field.getType())) {
                        //collectionFactory.create(field.getType(), field.getGenericType(), this);
                    }

                    field.setAccessible(true);
                    field.set(result, random(field.getType()));
                } catch (SecurityException e) {
                    throw new RandomizerException("Unable to access field " + field.getName() + " on object of type " + type.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new RandomizerException("Unable to set field " + field.getName() + " on object of type " + type.getName(), e);
                }
            }
            return result;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RandomizerException("Unable to create object of type " + type.getName(), e);
        }
    }

    private static int randomLength() {
        return 1 + new Random().nextInt(MAX_COLLECTION_SIZE);
    }
}

