package com.github.nylle.javafixture.generic;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TypeCaptureTest {

    @Test
    void create() {

        var expected = new FixtureType<Optional<String>>(){};

        ParameterizedType actual = TypeCapture.create(expected.asClass(), expected.getGenericTypeArguments());

        assertThat(actual.getRawType()).isEqualTo(expected.asClass());
        assertThat(actual.getActualTypeArguments()).isEqualTo(expected.getGenericTypeArguments());
    }

    @Test
    void rawTypeMustNotBeNull() {

        var expected = new FixtureType<Optional<String>>(){};

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TypeCapture.create(null, expected.getGenericTypeArguments()))
                .withMessageContaining("rawType: null")
                .withNoCause();
    }

    @Test
    void actualTypeArgumentsMustNotBeNull() {

        var expected = new FixtureType<Optional<String>>(){};

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TypeCapture.create(expected.asClass(), null))
                .withMessageContaining("actualTypeArguments: null")
                .withNoCause();
    }

    @Test
    void actualTypeArgumentsMustMatchTypeParameters() {

        var expected = new FixtureType<Optional<String>>(){};

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TypeCapture.create(expected.asClass(), new Type[0]))
                .withMessageContaining("actualTypeArguments do not match rawType type parameters (1): 0")
                .withNoCause();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TypeCapture.create(String.class, expected.getGenericTypeArguments()))
                .withMessageContaining("actualTypeArguments do not match rawType type parameters (0): 1")
                .withNoCause();
    }
}