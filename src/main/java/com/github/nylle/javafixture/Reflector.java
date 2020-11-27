package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Reflector<T> {
    private final T instance;
    private final SpecimenFactory specimenFactory;

    public Reflector(T instance, SpecimenFactory specimenFactory) {
        this.instance = instance;
        this.specimenFactory = specimenFactory;
    }

    public Reflector<T> validateCustomization(CustomizationContext customizationContext, SpecimenType<T> type) {
        var declaredFields = getDeclaredFields().map(field -> field.getName()).collect(toList());

        var missingDeclaredField = Stream.concat(customizationContext.getCustomFields().keySet().stream(), customizationContext.getIgnoredFields().stream())
                .filter(entry -> !declaredFields.contains(entry))
                .findFirst();

        if(missingDeclaredField.isPresent()) {
            throw new SpecimenException(format("Cannot customize field '%s': Field not found in class '%s'.", missingDeclaredField.get(), type.getName()));
        }

        return this;
    }

    public T populate(CustomizationContext customizationContext) {
        return populate(customizationContext, field -> field.getGenericType(), Map.of());
    }

    public T populate(CustomizationContext customizationContext, Map<String, ISpecimen<?>> specimens) {
        return populate(customizationContext, field -> field.getType(), specimens);
    }

    private T populate(CustomizationContext customizationContext, Function<Field, Type> getTypeFromField, Map<String, ISpecimen<?>> specimens) {
        getDeclaredFields()
                .collect(groupingBy(field -> field.getName()))
                .values()
                .forEach(values -> {
                    values.stream()
                            .findFirst()
                            .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                            .ifPresent(field -> setField(
                                    field,
                                    customizationContext.getCustomFields().getOrDefault(
                                            field.getName(),
                                            specimens.getOrDefault(
                                                    field.getGenericType().getTypeName(),
                                                    specimenFactory.build(SpecimenType.fromClass(getTypeFromField.apply(field)))).create())));
                    values.stream()
                            .skip(1)
                            .forEach(field -> setField(
                                    field,
                                    specimens.getOrDefault(
                                            field.getGenericType().getTypeName(),
                                            specimenFactory.build(SpecimenType.fromClass(getTypeFromField.apply(field)))).create()));
                });

        return instance;
    }

    private Stream<Field> getDeclaredFields() {
        return getDeclaredFields(instance.getClass())
                .filter(field -> !Modifier.isStatic(field.getModifiers()));
    }

    private Stream<Field> getDeclaredFields(Class<?> type) {
        return Stream.concat(
                Stream.of(type.getDeclaredFields()),
                Optional.ofNullable(type.getSuperclass())
                        .map(superclass -> getDeclaredFields(superclass))
                        .orElse(Stream.of()));
    }

    private void setField(Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        }
    }
}
