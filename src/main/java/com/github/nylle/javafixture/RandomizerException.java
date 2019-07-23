package com.github.nylle.javafixture;

public class RandomizerException extends RuntimeException {

    public RandomizerException(final String message) {
        super(message);
    }

    public RandomizerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
