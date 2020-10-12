package com.github.nylle.javafixture.annotations.fixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.github.nylle.javafixture.testobjects.example.Contract;

public class TestWithFixtureTest {
    @TestWithFixture
    void injectParameterViaMethodExtension(Contract contract, int intValue, Optional<String> optionalString) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
        assertThat(optionalString).isPresent();
        assertThat(optionalString.get()).isInstanceOf(String.class);
    }

    @TestWithFixture(minCollectionSize = 11, maxCollectionSize = 11, positiveNumbersOnly = true)
    void configurableFixture(List<Integer> input) {
        assertThat(input).hasSize(11);
        assertThat(input).allMatch(x -> x > 1);
    }
}
