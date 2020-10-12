package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpecimenTypeExceptionTest {

    @Test
    void withMessage() {
        var sut = new SpecimenTypeException("message");

        assertThat(sut.getMessage()).isEqualTo("message");
    }
}