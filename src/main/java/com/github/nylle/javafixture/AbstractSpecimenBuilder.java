package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractSpecimenBuilder<T> implements ISpecimenBuilder<T> {
    private final List<Consumer<T>> functions = new LinkedList<>();
    private final List<String> ignoredFields = new LinkedList<>();
    private final Map<String, Object> customFields = new HashMap<>();

    @Override
    public ISpecimenBuilder<T> with(Consumer<T> function) {
        functions.add(function);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> with(String fieldName, Object value) {
        customFields.put(fieldName, value);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> without(String fieldName) {
        ignoredFields.add(fieldName);
        return this;
    }

    protected T customize(T instance) {
        customFields.forEach((fieldName, value) -> ReflectionHelper.setField(fieldName, instance, value));
        ignoredFields.forEach(field -> ReflectionHelper.unsetField(field, instance));
        functions.forEach(f -> f.accept(instance));
        return instance;
    }

}
