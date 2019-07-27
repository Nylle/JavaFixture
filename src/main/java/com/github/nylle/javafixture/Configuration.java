package com.github.nylle.javafixture;

import java.util.concurrent.ThreadLocalRandom;

public class Configuration {
    private int maxCollectionSize = 10;
    private int minCollectionSize = 2;
    private int streamSize = 3;

    public Configuration() {
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

    public static Configuration configure() {
        return new Configuration();
    }

    public Configuration streamSize(int streamSize) {
        this.streamSize = streamSize;
        return this;
    }

    public Configuration maxCollectionSize(int maxCollectionSize) {
        this.maxCollectionSize = maxCollectionSize;
        return this;
    }

    public Configuration minCollectionSize(int minCollectionSize) {
        this.minCollectionSize = minCollectionSize;
        return this;
    }
}
