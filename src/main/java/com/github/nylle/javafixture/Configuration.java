package com.github.nylle.javafixture;

import java.util.concurrent.ThreadLocalRandom;

public class Configuration {
    private final int maxCollectionSize;
    private final int minCollectionSize;
    private final int streamSize;

    public Configuration() {
        this.maxCollectionSize = 10;
        this.minCollectionSize = 2;
        this.streamSize = 3;
    }

    public Configuration(int maxCollectionSize, int minCollectionSize, int streamSize) {
        this.maxCollectionSize = maxCollectionSize;
        this.minCollectionSize = minCollectionSize;
        this.streamSize = streamSize;
    }

    public int getMaxCollectionSize() {
        return maxCollectionSize;
    }

    public int getMinCollectionSize() {
        return minCollectionSize;
    }

    public int getStreamSize() {
        return streamSize;
    }

    public int getRandomCollectionSize() {
        return ThreadLocalRandom.current().nextInt(minCollectionSize, maxCollectionSize + 1);
    }

}
