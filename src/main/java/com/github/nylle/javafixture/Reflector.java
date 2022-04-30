package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Reflector<T> {
    private final T instance;

    public Reflector(T instance) {
        this.instance = instance;
    }

    public Reflector<T> validateCustomization(CustomizationContext customizationContext, SpecimenType<T> type) {
        var declaredFields = getDeclaredFields(instance.getClass()).map(field -> field.getName()).collect(toList());

        var missingDeclaredField = Stream.concat(customizationContext.getCustomFields().keySet().stream(), customizationContext.getIgnoredFields().stream())
                .filter(entry -> !declaredFields.contains(entry))
                .findFirst();

        if (missingDeclaredField.isPresent()) {
            throw new SpecimenException(format("Cannot customize field '%s': Field not found in class '%s'.", missingDeclaredField.get(), type.getName()));
        }

        var duplicateField = getDeclaredFields(instance.getClass())
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
        return getDeclaredFields(instance.getClass());
    }

    private Stream<Field> getDeclaredFields(Class<?> type) {
        return Stream.concat(
                Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())),
                Optional.ofNullable(type.getSuperclass())
                        .map(superclass -> getDeclaredFields(superclass))
                        .orElse(Stream.of()));
    }

    public void setField(Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        } catch (IllegalAccessException | InaccessibleObjectException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        }
    }
}
