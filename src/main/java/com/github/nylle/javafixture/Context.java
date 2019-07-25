package com.github.nylle.javafixture;

public class Context {
    private final Configuration configuration;

    public Context(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}

