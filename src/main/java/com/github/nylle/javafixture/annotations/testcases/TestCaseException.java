package com.github.nylle.javafixture.annotations.testcases;

public class TestCaseException extends RuntimeException {

    public TestCaseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
