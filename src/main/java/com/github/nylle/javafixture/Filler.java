package com.github.nylle.javafixture;

import java.lang.reflect.Field;

public class Filler {

    public <T> void fill(final T instance) {
        var type = instance.getClass();

        for (Field field : type.getDeclaredFields()) {
            if (Reflector.isStatic(field)) {
                continue;
            }

            if(field.getType().equals(type)) {
                setField(instance, field, instance);
                continue;
            }



                if(Reflector.isCollection(field.getType())) {
                    //field.set(result, collectionFactory.create(field.getType(), field.getGenericType(), this));
                } else {
                    //field.set(result, random(field.getType()));
                }
        }

    }

    private <T> void setField(T instance, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new SpecimenException("Unable to set field " + field.getName() + " on object of type " + instance.getClass().getName(), e);
        }
    }


}
