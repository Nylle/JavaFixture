package com.github.nylle.javafixture;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecimenTypeExceptionTest {

    @Test
    void withMessage() {
        SpecimenTypeException sut = new SpecimenTypeException("message");

        assertThat(sut.getMessage()).isEqualTo("message");
    }
}
