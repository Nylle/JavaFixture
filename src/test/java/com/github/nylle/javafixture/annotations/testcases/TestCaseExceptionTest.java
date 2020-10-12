package com.github.nylle.javafixture.annotations.testcases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestCaseExceptionTest {

    @Test
    void withMessageAndCause() {
        var sut = new TestCaseException("message", new NullPointerException("null"));

        assertThat(sut.getMessage()).isEqualTo("message");
        assertThat(sut.getCause()).isInstanceOf(NullPointerException.class);
        assertThat(sut.getCause().getMessage()).isEqualTo("null");
    }


}