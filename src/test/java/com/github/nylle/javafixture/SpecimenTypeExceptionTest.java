package com.github.nylle.javafixture;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecimenTypeExceptionTest {

    @Test
    void withMessage() {
        var sut = new SpecimenTypeException("message");

        assertThat(sut.getMessage()).isEqualTo("message");
    }
}
