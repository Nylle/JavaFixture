package com.github.nylle.javafixture;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class Configuration {
    private int maxCollectionSize = 10;
    private int minCollectionSize = 2;
    private int streamSize = 3;

    private Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    /**
     * Creates a new default configuration with the following values
     *
     * maxCollectionSize = 10
     * minCollectionSize = 2
     * streamSize = 3
     * clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     */
    public Configuration() {
    }

    /**
     * Creates a new configuration with the specified values
     *
     * @param maxCollectionSize
     * @param minCollectionSize
     * @param streamSize
     */
    public Configuration(int maxCollectionSize, int minCollectionSize, int streamSize) {
        this.maxCollectionSize = maxCollectionSize;
        this.minCollectionSize = minCollectionSize;
        this.streamSize = streamSize;
    }

    /**
     * Returns the maximum size of collections and maps
     * @return
     */
    public int getMaxCollectionSize() {
        return maxCollectionSize;
    }

    /**
     * Returns the minimum size of collections and maps
     * @return
     */
    public int getMinCollectionSize() {
        return minCollectionSize;
    }

    /**
     * Returns the stream size when creating many objects at once
     * @return
     */
    public int getStreamSize() {
        return streamSize;
    }

    /**
     * Returns the clock used when creating time-based objects.
     * @return
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Returns a random value between minimum and maximum size of collections and maps
     * @return
     */
    public int getRandomCollectionSize() {
        return ThreadLocalRandom.current().nextInt(minCollectionSize, maxCollectionSize + 1);
    }

    /**
     * Creates a new default configuration with the following values
     *
     * maxCollectionSize = 10
     * minCollectionSize = 2
     * streamSize = 3
     * clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
     */
    public static Configuration configure() {
        return new Configuration();
    }

    /**
     * Sets the stream size when creating many objects at once
     * @param streamSize
     * @return
     */
    public Configuration streamSize(int streamSize) {
        this.streamSize = streamSize;
        return this;
    }

    /**
     * Sets the minimum and maximum length for collections and maps
     * @param min
     * @param max
     * @return
     */
    public Configuration collectionSizeRange(int min, int max) {
        this.minCollectionSize = min;
        this.maxCollectionSize = max;
        return this;
    }

    public Configuration clock(Clock clock) {
        this.clock = clock;
        return this;
    }
}
