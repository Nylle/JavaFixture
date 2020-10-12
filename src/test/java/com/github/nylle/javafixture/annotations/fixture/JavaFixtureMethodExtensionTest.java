package com.github.nylle.javafixture.annotations.fixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.nylle.javafixture.testobjects.example.Contract;

public class JavaFixtureMethodExtensionTest {

    @Test
    @ExtendWith(JavaFixtureExtension.class)
    void injectParameterViaMethodExtension(Contract contract, int intValue) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
