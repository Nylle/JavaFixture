package com.github.nylle.javafixture;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class Configuration {
    private int maxCollectionSize = 10;
    private int minCollectionSize = 2;
    private int streamSize = 3;
    private boolean usePositiveNumbersOnly = false;
    private boolean experimentalInterfaces = false;

    private Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    /**
     * Creates a new default configuration with the following values
     * <p><ul>
     * <li>maxCollectionSize = 10
     * <li>minCollectionSize = 2
     * <li>streamSize = 3
     * <li>usePositiveNumbersOnly = false
     * <li>clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     * </ul><p>
     */
    public Configuration() {
        this.experimentalInterfaces = experimentalInterfacesIsEnabled();
    }

    /**
     * Creates a new configuration with the specified values
     *
     * @param maxCollectionSize the maximum size of arrays, collections and maps
     * @param minCollectionSize the minimum size of arrays, collections and maps
     * @param streamSize the exact size of the result stream when creating many objects at once
     */
    public Configuration(final int maxCollectionSize, final int minCollectionSize, final int streamSize) {
        this();
        this.maxCollectionSize = maxCollectionSize;
        this.minCollectionSize = minCollectionSize;
        this.streamSize = streamSize;
        this.usePositiveNumbersOnly = false;
    }

    /**
     * Creates a new configuration with the specified values
     *
     * @param maxCollectionSize the maximum size of arrays, collections and maps
     * @param minCollectionSize the minimum size of arrays, collections and maps
     * @param streamSize the exact size of the result stream when creating many objects at once
     * @param usePositiveNumbersOnly whether to generate only positive numbers including 0
     */
    public Configuration(final int maxCollectionSize, final int minCollectionSize, final int streamSize, final boolean usePositiveNumbersOnly) {
        this(maxCollectionSize, minCollectionSize, streamSize);
        this.usePositiveNumbersOnly = usePositiveNumbersOnly;
    }

    /**
     * Creates a new default configuration with the following values
     * <p><ul>
     * <li>maxCollectionSize = 10
     * <li>minCollectionSize = 2
     * <li>streamSize = 3
     * <li>usePositiveNumbersOnly = false
     * <li>clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     * </ul><p>
     */
    public static Configuration configure() {
        return new Configuration();
    }

    /**
     * @return the maximum size of arrays, collections and maps
     */
    public int getMaxCollectionSize() {
        return maxCollectionSize;
    }

    /**
     * @return the minimum size of arrays, collections and maps
     */
    public int getMinCollectionSize() {
        return minCollectionSize;
    }

    /**
     * @return the stream size when creating many objects at once
     */
    public int getStreamSize() {
        return streamSize;
    }

    /**
     * @return the clock used when creating time-based objects
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * @return a random value between minimum and maximum size of arrays, collections and maps
     */
    public int getRandomCollectionSize() {
        return ThreadLocalRandom.current().nextInt(minCollectionSize, maxCollectionSize + 1);
    }

    /**
     * @return whether to generate only positive numbers including 0
     */
    public boolean usePositiveNumbersOnly() {
        return this.usePositiveNumbersOnly;
    }

    public boolean experimentalInterfaces() {
        return this.experimentalInterfaces;
    }

    /**
     * @param streamSize the stream size when creating many objects at once
     * @return this {@code Configuration}
     */
    public Configuration streamSize(int streamSize) {
        this.streamSize = streamSize;
        return this;
    }

    /**
     * Sets the minimum and maximum length for arrays, collections and maps
     *
     * @param min the minimum size of arrays, collections and maps
     * @param max the maximum size of arrays, collections and maps
     * @return this {@code Configuration}
     */
    public Configuration collectionSizeRange(int min, int max) {
        this.minCollectionSize = min;
        this.maxCollectionSize = max;
        return this;
    }

    /**
     * @param clock the clock to use when generating time-based objects
     * @return this {@code Configuration}
     */
    public Configuration clock(Clock clock) {
        this.clock = clock;
        return this;
    }

    /**
     * @param usePositiveNumbersOnly whether to generate only positive numbers including 0
     * @return this {@code Configuration}
     */
    public Configuration usePositiveNumbersOnly(boolean usePositiveNumbersOnly) {
        this.usePositiveNumbersOnly = usePositiveNumbersOnly;
        return this;
    }

    public Configuration experimentalInterfaces(boolean experimentalInterfaces) {
        this.experimentalInterfaces = experimentalInterfaces;
        return this;
    }

    /**
     * Returns a new fixture with this configuration
     *
     * @return {@code Fixture}
     */
    public Fixture fixture() {
        return new Fixture(this);
    }

    private boolean experimentalInterfacesIsEnabled() {
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("javafixture/com.github.nylle.javafixture.experimetalInterfaces")) {
            if(in == null) {
                return false;
            }
            return new BufferedReader(new InputStreamReader(in)).lines().findFirst().map(x -> x.equals("enabled")).orElse(false);
        } catch(Exception ex) {
            return false;
        }
    }
}
