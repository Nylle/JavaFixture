package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;

public interface Instantiator<T> {

    Result<T> invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext);

    class Result<T> {
        private T value;
        private String message;

        private Result(T value, String message) {
            this.value = value;
            this.message = message;
        }

        public static <T> Result<T> of(T value) {
            if(value == null) {
                return new Result<T>(null, "result was null");
            }
            return new Result<T>(value, null);
        }

        public static <T> Result<T> empty(String message) {
            return new Result<T>(null, message);
        }

        public T getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public boolean isPresent() {
            return value != null;
        }

        public boolean isEmpty() {
            return value == null;
        }
    }
}
