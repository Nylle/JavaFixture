package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static com.github.nylle.javafixture.Configuration.configure;
import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static com.github.nylle.javafixture.SpecimenType.fromClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrimitiveSpecimenTest {

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new PrimitiveSpecimen<>(SpecimenType.fromClass(Integer.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new PrimitiveSpecimen<>(null, new Context(configure())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void onlyPrimitiveTypes() {
        assertThatThrownBy(() -> new PrimitiveSpecimen<>(fromClass(Object.class), new Context(configure())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Object.class.getName());
    }

    @Test
    void createString() {
        var sut = new PrimitiveSpecimen<String>(fromClass(String.class), new Context(configure()));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(String.class);
        assertThat(actual.length()).isGreaterThan(0);
    }

    @Test
    void createBoolean() {
        var sut = new PrimitiveSpecimen<>(fromClass(boolean.class), new Context(configure()));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Boolean.class);
        assertThat(actual).isIn(true, false);
    }

    @Test
    void createByte() {
        var sut = new PrimitiveSpecimen<>(fromClass(byte.class), new Context(configure()));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Byte.class);
        assertThat(actual.toString().length()).isGreaterThan(0);
    }

    @TestWithCases
    @TestCase(bool1 = false, short2 = Short.MIN_VALUE, short3 = Short.MAX_VALUE)
    @TestCase(bool1 = true, short2 = 0, short3 = Short.MAX_VALUE)
    void createShort(boolean positiveOnly, short min, short max) {
        var sut = new PrimitiveSpecimen<Short>(fromClass(short.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Short.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, int2 = Integer.MIN_VALUE, int3 = Integer.MAX_VALUE)
    @TestCase(bool1 = true, int2 = 0, int3 = Integer.MAX_VALUE)
    void createInteger(boolean positiveOnly, int min, int max) {
        var sut = new PrimitiveSpecimen<Integer>(fromClass(int.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Integer.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, long2 = Long.MIN_VALUE, long3 = Long.MAX_VALUE)
    @TestCase(bool1 = true, long2 = 0, long3 = Long.MAX_VALUE)
    void createLong(boolean positiveOnly, long min, long max) {
        var sut = new PrimitiveSpecimen<Long>(fromClass(long.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Long.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, float2 = Float.MIN_VALUE, float3 = Float.MAX_VALUE)
    @TestCase(bool1 = true, float2 = 0, float3 = Float.MAX_VALUE)
    void createFloat(boolean positiveOnly, float min, float max) {
        var sut = new PrimitiveSpecimen<Float>(fromClass(float.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Float.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, double2 = Double.MIN_VALUE, double3 = Double.MAX_VALUE)
    @TestCase(bool1 = true, double2 = 0, double3 = Double.MAX_VALUE)
    void createDouble(boolean positiveOnly, double min, double max) {
        var sut = new PrimitiveSpecimen<Double>(fromClass(double.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Double.class);
        assertThat(actual).isBetween(min, max);
    }

}
