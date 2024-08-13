package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Fixture {

    private final Configuration configuration;

    /**
     * Creates a new {@code Fixture} with default configuration:
     * <p><ul>
     * <li>maxCollectionSize = 10
     * <li>minCollectionSize = 2
     * <li>streamSize = 3
     * <li>usePositiveNumbersOnly = false
     * <li>clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     * </ul><p>
     */
    public Fixture() {
        this(new Configuration());
    }

    /**
     * Creates a new {@code Fixture} with given configuration
     *
     * @param configuration the configuration
     */
    public Fixture(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates a new {@code Fixture} with default configuration:
     * <p><ul>
     * <li>maxCollectionSize = 10
     * <li>minCollectionSize = 2
     * <li>streamSize = 3
     * <li>usePositiveNumbersOnly = false
     * <li>clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     * </ul><p>
     */
    public static Fixture fixture() {
        return new Fixture();
    }

    /**
     * Returns a new default configuration with the following values
     * <p><ul>
     * <li>maxCollectionSize = 10
     * <li>minCollectionSize = 2
     * <li>streamSize = 3
     * <li>usePositiveNumbersOnly = false
     * <li>clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     * </ul><p>
     *
     * @return {@code Configuration}
     */
    public static Configuration configuration() {
        return new Configuration();
    }

    /**
     * Creates a new object of the specified type, recursively populated with random values
     *
     * @param type the {@code Class<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a new object of the specified {@code Class<T>}
     */
    public <T> T create(final Class<T> type) {
        return create(SpecimenType.fromClass(type));
    }

    /**
     * Creates a new optional of the specified type, recursively populated with random values
     *
     * @param type the {@code Class<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a new {@code Optional} of the specified {@code Class<T>}
     * <p>
     * This feature is deprecated without replacement.
     */
    @Deprecated(forRemoval = true)
    public <T> Optional<T> createOptional(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).createOptional();
    }

    /**
     * Creates a new object of the specified type, recursively populated with random values
     *
     * @param type the {@code SpecimenType<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a new object of the specified type {@code T}
     */
    public <T> T create(final SpecimenType<T> type) {
        var res = new SpecimenBuilder<>(type, configuration).create();
        if (type.isCollection()) {
            var o = ((Collection<?>) res).toArray()[0];
            var elems = new SpecimenBuilder<>(
                    SpecimenType.fromClass(
                            o.getClass()),
                    configuration).createMany(configuration.getRandomCollectionSize())
                    .collect(Collectors.toList());
            ((Collection) res).clear();
            ((Collection) res).addAll(elems);
        }
        return res;
    }

    /**
     * Creates a new object of the specified type, using a random constructor if available
     *
     * @param type the {@code Class<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a new object of the specified type {@code T}
     */
    public <T> T construct(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).construct();
    }

    /**
     * Creates a new object of the specified type, using a random constructor if available
     *
     * @param type the {@code SpecimenType<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a new object of the specified type {@code T}
     */
    public <T> T construct(final SpecimenType<T> type) {
        return new SpecimenBuilder<>(type, configuration).construct();
    }

    /**
     * Creates a {@code Stream} of objects of the specified type, recursively populated with random values
     *
     * @param type the {@code Class<T>} based on which the objects are created
     * @param <T>  the type of the objects to be created
     * @return a {@code Stream} of objects of the specified type {@code T}
     */
    public <T> Stream<T> createMany(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).createMany();
    }

    /**
     * Creates a {@code Stream} of objects of the specified type, recursively populated with random values
     *
     * @param type the {@code SpecimenType<T>} based on which the objects are created
     * @param <T>  the type of the objects to be created
     * @return a {@code Stream} of objects of the specified type {@code T}
     */
    public <T> Stream<T> createMany(final SpecimenType<T> type) {
        return new SpecimenBuilder<>(type, configuration).createMany();
    }

    /**
     * Creates a {@code ISpecimenBuilder<T>} to customise the object of type {@code T} to be created
     *
     * @param type the {@code Class<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a builder for further customisation
     */
    public <T> ISpecimenBuilder<T> build(final Class<T> type) {
        return new SpecimenBuilder<>(SpecimenType.fromClass(type), configuration);
    }

    /**
     * Creates a {@code ISpecimenBuilder<T>} to customise the object of type {@code T} to be created
     *
     * @param type the {@code SpecimenType<T>} based on which the object is created
     * @param <T>  the type of the object to be created
     * @return a builder for further customisation
     */
    public <T> ISpecimenBuilder<T> build(final SpecimenType<T> type) {
        return new SpecimenBuilder<>(type, configuration);
    }

    /**
     * Adds objects of the specified {@code Class<T>}, recursively populated with random values, to the specified {@code Collection<T>}
     * The number of objects created is specified in the {@code Configuration} under {@code streamSize} (default: 3)
     *
     * @param result the collection the created objects should be added to
     * @param type   the {@code Class<T>} based on which the objects are created
     * @param <T>    the type of the collection and the objects to be added
     */
    public <T> void addManyTo(Collection<T> result, final Class<T> type) {
        result.addAll(new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).createMany().collect(Collectors.toList()));
    }

    /**
     * Adds objects of the specified {@code SpecimenType<T>}, recursively populated with random values, to the specified {@code Collection<T>}
     * The number of objects created is specified in the {@code Configuration} under {@code streamSize} (default: 3)
     *
     * @param result the collection the created objects should be added to
     * @param type   the {@code SpecimenType<T>} based on which the objects are created
     * @param <T>    the type of the collection and the objects to be added
     */
    public <T> void addManyTo(Collection<T> result, final SpecimenType<T> type) {
        result.addAll(new SpecimenBuilder<>(type, configuration).createMany().collect(Collectors.toList()));
    }
}
