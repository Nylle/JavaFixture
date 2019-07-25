package com.github.nylle.javafixture;

public class SpecimenException extends RuntimeException {

    public SpecimenException(final String message) {
        super(message);
    }

    public SpecimenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
