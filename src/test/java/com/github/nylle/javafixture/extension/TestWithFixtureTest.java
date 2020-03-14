package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.testobjects.example.Contract;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWithFixtureTest {
    @TestWithFixture
    void injectParameterViaMethodExtension(Contract contract, int intValue, Optional<String> optionalString) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
        assertThat(optionalString).isPresent();
        assertThat(optionalString.get()).isInstanceOf(String.class);
    }
}
