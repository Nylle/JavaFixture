package com.github.nylle.javafixture;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SpecimenBuilder<T> implements ISpecimenBuilder<T> {
    private final List<Consumer<T>> functions = new LinkedList<>();
    private final List<String> ignoredFields = new LinkedList<>();
    private final Map<String, Object> customFields = new HashMap<>();
    private final Map<SpecimenType<?>, Object> predefinedInstances = new ConcurrentHashMap<>();

    private final SpecimenType<T> type;
    private final Configuration configuration;

    public SpecimenBuilder(final SpecimenType<T> type, final Configuration configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    /**
     * @return a new object based on this {@code ISpecimenBuilder<T>}
     */
    @Override
    public T create() {
        return customize(new SpecimenFactory(new Context(configuration, predefinedInstances)).build(type).create(new CustomizationContext(ignoredFields, customFields), new Annotation[0]));
    }

    /**
     * @return a new {@code Optional<T>} based on this {@code ISpecimenBuilder<T>}
     *
     * This feature is deprecated without replacement.
     */
    @Deprecated(forRemoval = true)
    @Override
    public Optional<T> createOptional() {
        return Optional.of(create());
    }

    /**
     * @return a {@code Stream} of objects based on this {@code ISpecimenBuilder<T>}
     */
    @Override
    public Stream<T> createMany() {
        return createMany(configuration.getStreamSize());
    }

    /**
     * Creates a {@code Stream} of objects based on this {@code ISpecimenBuilder<T>} with the specified size
     *
     * @param size the size of the {@code Stream} to be created
     * @return a {@code Stream} of objects based on this {@code ISpecimenBuilder<T>}
     */
    @Override
    public Stream<T> createMany(final int size) {
        return IntStream.range(0, size).boxed().map(x -> create());
    }

    /**
     * Applies the specified function to the created object
     *
     * @param function a function to customise the created object
     * @return this builder for further customisation
     */
    @Override
    public ISpecimenBuilder<T> with(final Consumer<T> function) {
        functions.add(function);
        return this;
    }

    /**
     * Sets the field with the specified name to the specified value during object creation
     *
     * @param fieldName the name of the field to be set
     * @param value the value to be set to the field
     * @return this builder for further customisation
     */
    @Override
    public ISpecimenBuilder<T> with(final String fieldName, Object value) {
        customFields.put(fieldName, value);
        return this;
    }

    /**
     * Sets all fields with the specified type to the specified value during object creation.
     *
     * @param type the type of the fields to be set
     * @param value the value to be set to the fields
     * @param <U> the type of the value
     * @return this builder for further customisation
     */
    @Override
    public <U> ISpecimenBuilder<T> with(final Class<U> type, final U value) {
        predefinedInstances.putIfAbsent(SpecimenType.fromClass(type), value);
        return this;
    }

    /**
     * Sets all fields with the specified type to the specified value during object creation.
     *
     * @param type the type of the fields to be set
     * @param value the value to be set to the fields
     * @param <U> the type of the value
     * @return this builder for further customisation
     */
    @Override
    public <U> ISpecimenBuilder<T> with(final SpecimenType<U> type, final U value) {
        predefinedInstances.putIfAbsent(type, value);
        return this;
    }

    /**
     * Omits the field with the specified name during object creation
     * Primitives will receive their respective default-value, objects will be null
     *
     * @param fieldName the name of the field to be set
     * @return this builder for further customisation
     */
    @Override
    public ISpecimenBuilder<T> without(final String fieldName) {
        ignoredFields.add(fieldName);
        return this;
    }

    T construct() {
        return new SpecimenFactory(new Context(configuration)).build(type).create(new CustomizationContext(true), new Annotation[0]);
    }

    private T customize(T instance) {
        functions.forEach(f -> f.accept(instance));
        return instance;
    }

}

