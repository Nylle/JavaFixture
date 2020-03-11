package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.testobjects.example.Contract;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWithFixtureTest {
    @TestWithFixture
    void injectParameterViaMethodExtension(Contract contract, int intValue) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

}
