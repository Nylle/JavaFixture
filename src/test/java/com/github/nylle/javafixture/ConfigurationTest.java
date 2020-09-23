package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ConfigurationTest {

    @Test
    void canCreateAFixtureWithGivenConfiguration() {

        var result = new Configuration()
                .collectionSizeRange(1, 1)
                .fixture()
                .create(new SpecimenType<List<Integer>>() {});

        assertThat(result).hasSize(1);
    }

}