package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.github.nylle.javafixture.Configuration.configure;
import static com.github.nylle.javafixture.Fixture.fixture;
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
        PrimitiveSpecimen<String> sut = new PrimitiveSpecimen<String>(fromClass(String.class), new Context(configure()));

        String actual = sut.create();

        assertThat(actual).isInstanceOf(String.class);
        assertThat(actual.length()).isGreaterThan(0);
    }

    @Test
    void createBoolean() {
        PrimitiveSpecimen<Boolean> sut = new PrimitiveSpecimen<>(fromClass(boolean.class), new Context(configure()));

        Boolean actual = sut.create();

        assertThat(actual).isInstanceOf(Boolean.class);
        assertThat(actual).isIn(true, false);
    }

    @Test
    void createByte() {
        PrimitiveSpecimen<Byte> sut = new PrimitiveSpecimen<>(fromClass(byte.class), new Context(configure()));

        Byte actual = sut.create();

        assertThat(actual).isInstanceOf(Byte.class);
        assertThat(actual.toString().length()).isGreaterThan(0);
    }

    @TestWithCases
    @TestCase(bool1 = false, short2 = Short.MIN_VALUE, short3 = Short.MAX_VALUE)
    @TestCase(bool1 = true, short2 = 0, short3 = Short.MAX_VALUE)
    void createShort(boolean positiveOnly, short min, short max) {
        PrimitiveSpecimen<Short> sut = new PrimitiveSpecimen<Short>(fromClass(short.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        Short actual = sut.create();

        assertThat(actual).isInstanceOf(Short.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, int2 = Integer.MIN_VALUE, int3 = Integer.MAX_VALUE)
    @TestCase(bool1 = true, int2 = 0, int3 = Integer.MAX_VALUE)
    void createInteger(boolean positiveOnly, int min, int max) {
        PrimitiveSpecimen<Integer> sut = new PrimitiveSpecimen<Integer>(fromClass(int.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        Integer actual = sut.create();

        assertThat(actual).isInstanceOf(Integer.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, long2 = Long.MIN_VALUE, long3 = Long.MAX_VALUE)
    @TestCase(bool1 = true, long2 = 0, long3 = Long.MAX_VALUE)
    void createLong(boolean positiveOnly, long min, long max) {
        PrimitiveSpecimen<Long> sut = new PrimitiveSpecimen<Long>(fromClass(long.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        Long actual = sut.create();

        assertThat(actual).isInstanceOf(Long.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, float2 = Float.MIN_VALUE, float3 = Float.MAX_VALUE)
    @TestCase(bool1 = true, float2 = 0, float3 = Float.MAX_VALUE)
    void createFloat(boolean positiveOnly, float min, float max) {
        PrimitiveSpecimen<Float> sut = new PrimitiveSpecimen<Float>(fromClass(float.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        Float actual = sut.create();

        assertThat(actual).isInstanceOf(Float.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, double2 = Double.MIN_VALUE, double3 = Double.MAX_VALUE)
    @TestCase(bool1 = true, double2 = 0, double3 = Double.MAX_VALUE)
    void createDouble(boolean positiveOnly, double min, double max) {
        PrimitiveSpecimen<Double> sut = new PrimitiveSpecimen<Double>(fromClass(double.class), new Context(configure().usePositiveNumbersOnly(positiveOnly)));

        Double actual = sut.create();

        assertThat(actual).isInstanceOf(Double.class);
        assertThat(actual).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(class1 = String.class)
    @TestCase(class1 = Boolean.class)
    @TestCase(class1 = Character.class)
    @TestCase(class1 = Byte.class)
    @TestCase(class1 = Short.class)
    @TestCase(class1 = Integer.class)
    @TestCase(class1 = Long.class)
    @TestCase(class1 = Float.class)
    @TestCase(class1 = Double.class)
    void canBePredefined(Class type) {
        Object expected = fixture().create(type);

        HashMap<Integer, Object> map = new HashMap<>();
        map.put(SpecimenType.fromClass(type).hashCode(), expected);

        Context context = new Context(Configuration.configure(), map);

        PrimitiveSpecimen sut = new PrimitiveSpecimen<>(SpecimenType.fromClass(type), context);

        Object actual = sut.create();

        assertThat(actual).isSameAs(expected);
    }

}
