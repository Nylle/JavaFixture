package com.github.nylle.javafixture.extensions.testcases;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestCaseExceptionTest {

    @Test
    void withMessageAndCause() {
        var sut = new TestCaseException("message", new NullPointerException("null"));

        assertThat(sut.getMessage()).isEqualTo("message");
        assertThat(sut.getCause()).isInstanceOf(NullPointerException.class);
        assertThat(sut.getCause().getMessage()).isEqualTo("null");
    }


}