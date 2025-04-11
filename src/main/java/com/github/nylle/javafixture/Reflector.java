package com.github.nylle.javafixture;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Reflector<T> {
    private final T instance;
    private final Class<?> clazz;

    public Reflector(T instance) {
        this(instance, instance.getClass());
    }

    public Reflector(T instance, Class<?> clazz) {
        this.instance = instance;
        this.clazz = clazz;
    }

    public Reflector<T> validateCustomization(CustomizationContext customizationContext, SpecimenType<T> type) {
        var declaredFields = getDeclaredFields(clazz).map(field -> field.getName()).collect(toList());

        var missingDeclaredField = Stream.concat(customizationContext.getCustomFields().keySet().stream(), customizationContext.getIgnoredFields().stream())
                .filter(entry -> !declaredFields.contains(entry))
                .findFirst();

        if (missingDeclaredField.isPresent()) {
            throw new SpecimenException(format("Cannot customize field '%s': Field not found in class '%s'.", missingDeclaredField.get(), type.getName()));
        }

        var duplicateField = getDeclaredFields(clazz)
                .collect(groupingBy(field -> field.getName()))
                .entrySet()
                .stream()
                .filter(x -> x.getValue().size() > 1)
                .filter(x -> Stream.concat(customizationContext.getCustomFields().keySet().stream(), customizationContext.getIgnoredFields().stream()).anyMatch(y -> y.equals(x.getKey())))
                .findFirst();

        if (duplicateField.isPresent()) {
            throw new SpecimenException(format("Cannot customize field '%s'. Duplicate field names found: \n%s",
                    duplicateField.get().getKey(),
                    duplicateField.get().getValue().stream().map(x -> x.toString()).collect(joining("\n"))));
        }

        return this;
    }

    public Stream<Field> getDeclaredFields() {
        return getDeclaredFields(clazz);
    }

    public Annotation[] getFieldAnnotations(Field field) {
        try {
            return Stream.concat(Arrays.stream(Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                    .filter(property -> !Modifier.isStatic(field.getModifiers()))
                    .filter(property -> property.getName().equals(field.getName()))
                    .flatMap(propertyDescriptor -> Stream.of(propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod())
                            .filter(x -> Objects.nonNull(x))
                            .flatMap(method -> Stream.of(method.getAnnotations()))), Arrays.stream(field.getAnnotations())).toArray(x -> new Annotation[x]);
        } catch (IntrospectionException e) {
            return field.getAnnotations();
        }
    }

    public void setField(Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", field.getName(), clazz.getName()), e);
        } catch (IllegalAccessException | InaccessibleObjectException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", field.getName(), clazz.getName()), e);
        }
    }

    private Stream<Field> getDeclaredFields(Class<?> type) {
        return Stream.concat(
                Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())),
                Optional.ofNullable(type.getSuperclass())
                        .map(superclass -> getDeclaredFields(superclass))
                        .orElse(Stream.of()));
    }
}
