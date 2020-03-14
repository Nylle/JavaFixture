package com.github.nylle.javafixture.extensions.fixture;

import com.github.nylle.javafixture.testobjects.example.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(JavaFixtureExtension.class)
public class JavaFixtureClassExtensionTest {

    private Contract contract;
    private int intValue;
    private Optional<String> optionalString;

    @BeforeEach
    void setup(Contract contract, int intValue, Optional<String> optionalString) {
        this.contract = contract;
        this.intValue = intValue;
        this.optionalString = optionalString;
    }

    @Test
    void injectParameterIntoSetup() {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
        assertThat(optionalString).isPresent();
        assertThat(optionalString.get()).isInstanceOf(String.class);
    }

    @Test
    void injectParameterViaClassExtension(Contract contract, int intValue, Optional<String> optionalString) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
        assertThat(optionalString).isPresent();
        assertThat(optionalString.get()).isInstanceOf(String.class);
    }
}
