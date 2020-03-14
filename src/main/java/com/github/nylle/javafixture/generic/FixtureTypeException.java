package com.github.nylle.javafixture.generic;

public class FixtureTypeException extends RuntimeException {

    public FixtureTypeException(final String message) {
        super(message);
    }

    public FixtureTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
