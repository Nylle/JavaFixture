package com.github.nylle.javafixture;

import java.util.concurrent.ThreadLocalRandom;

public class Configuration {
    private final int maxCollectionSize;
    private final int minCollectionSize;

    public Configuration() {
        this.maxCollectionSize = 10;
        this.minCollectionSize = 2;
    }

    public Configuration(int maxCollectionSize, int minCollectionSize) {
        this.maxCollectionSize = maxCollectionSize;
        this.minCollectionSize = minCollectionSize;
    }

    public int getMaxCollectionSize() {
        return maxCollectionSize;
    }

    public int getMinCollectionSize() {
        return minCollectionSize;
    }

    public int getRandomCollectionSize() {
        return ThreadLocalRandom.current().nextInt(minCollectionSize, maxCollectionSize + 1);
    }

}
