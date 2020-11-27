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

    private T populate(CustomizationContext customizationContext, Function<SpecimenField, Type> getTypeFromField, Map<String, ISpecimen<?>> specimens) {
        getDeclaredFields()
                .collect(groupingBy(field -> field.getName()))
                .values()
                .forEach(values -> {
                    values.stream()
                            .findFirst()
                            .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                            .ifPresent(field -> field.set(
                                    instance,
                                    customizationContext.getCustomFields().getOrDefault(
                                            field.getName(),
                                            specimens.getOrDefault(
                                                    field.getGenericType().getTypeName(),
                                                    specimenFactory.build(SpecimenType.fromClass(getTypeFromField.apply(field)))).create())));
                    values.stream()
                            .skip(1)
                            .forEach(field -> field.set(
                                    instance,
                                    specimens.getOrDefault(
                                            field.getGenericType().getTypeName(),
                                            specimenFactory.build(SpecimenType.fromClass(getTypeFromField.apply(field)))).create()));
                });

        return instance;
    }

    private Stream<SpecimenField> getDeclaredFields() {
        return getDeclaredFields(instance.getClass())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> new SpecimenField(field));
    }

    private Stream<Field> getDeclaredFields(Class<?> type) {
        return Stream.concat(
                Stream.of(type.getDeclaredFields()),
                Optional.ofNullable(type.getSuperclass())
                        .map(superclass -> getDeclaredFields(superclass))
                        .orElse(Stream.of()));
    }
}
