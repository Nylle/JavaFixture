package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.testobjects.complex.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(JavaFixtureExtension.class)
public class JavaFixtureClassExtensionTest {

    private Contract contract;
    private int intValue;

    @BeforeEach
    void setup(Contract contract, int intValue) {
        this.contract = contract;
        this.intValue = intValue;
    }

    @Test
    void injectParameterIntoSetup() {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Test
    void injectParameterViaClassExtension(Contract contract, int intValue) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

}
