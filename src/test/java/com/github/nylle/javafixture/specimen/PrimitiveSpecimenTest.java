package com.github.nylle.javafixture.specimen;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrimitiveSpecimenTest {

    @Test
    void onlyPrimitiveTypes() {
        assertThatThrownBy(() -> { new PrimitiveSpecimen<>(Object.class); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Object.class.getName());
    }

    @Test
    void createString() {
        var sut = new PrimitiveSpecimen<>(String.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(String.class);
        assertThat(actual.length()).isGreaterThan(0);
    }

    @Test
    void createBoolean() {
        var sut = new PrimitiveSpecimen<>(boolean.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Boolean.class);
        assertThat(actual).isIn(true, false);
    }

    @Test
    void createByte() {
        var sut = new PrimitiveSpecimen<>(byte.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Byte.class);
        assertThat(actual.toString().length()).isGreaterThan(0);
    }

    @Test
    void createShort() {
        var sut = new PrimitiveSpecimen<>(short.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Short.class);
        assertThat(actual).isBetween(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Test
    void createInteger() {
        var sut = new PrimitiveSpecimen<>(int.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Integer.class);
        assertThat(actual).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Test
    void createLong() {
        var sut = new PrimitiveSpecimen<>(long.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Long.class);
        assertThat(actual).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Test
    void createFloat() {
        var sut = new PrimitiveSpecimen<>(float.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Float.class);
        assertThat(actual).isBetween(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Test
    void createDouble() {
        var sut = new PrimitiveSpecimen<>(double.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Double.class);
        assertThat(actual).isBetween(Double.MIN_VALUE, Double.MAX_VALUE);
    }

}