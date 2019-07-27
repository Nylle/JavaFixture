package com.github.nylle.javafixture;

public class SpecimenBuilderProvider {

    private final Configuration configuration;

    public SpecimenBuilderProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> ISpecimenBuilder<T> newBuilder(final Class<T> typeReference) {
        return configuration.useEasyRandom()
                ? new EasyRandomBuilder<>(typeReference, configuration)
                : new SpecimenBuilder<>(typeReference, configuration);
    }

}
