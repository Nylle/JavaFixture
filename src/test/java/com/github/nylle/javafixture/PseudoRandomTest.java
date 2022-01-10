package com.github.nylle.javafixture;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.specimen.constraints.StringConstraints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PseudoRandomTest {

    @TestWithCases
    @TestCase(bool1 = false, short2 = Short.MIN_VALUE, short3 = Short.MAX_VALUE)
    @TestCase(bool1 = true, short2 = 0, short3 = Short.MAX_VALUE)
    void nextShort(boolean onlyPositive, short min, short max) {
        assertThat(new PseudoRandom().nextShort(onlyPositive)).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, int2 = Integer.MIN_VALUE, int3 = Integer.MAX_VALUE)
    @TestCase(bool1 = true, int2 = 0, int3 = Integer.MAX_VALUE)
    void nextInt(boolean onlyPositive, int min, int max) {
        assertThat(new PseudoRandom().nextInt(onlyPositive)).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, long2 = Long.MIN_VALUE, long3 = Long.MAX_VALUE)
    @TestCase(bool1 = true, long2 = 0, long3 = Long.MAX_VALUE)
    void nextLong(boolean onlyPositive, long min, long max) {
        assertThat(new PseudoRandom().nextLong(onlyPositive)).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, float2 = Float.MIN_VALUE, float3 = Float.MAX_VALUE)
    @TestCase(bool1 = true, float2 = 0, float3 = Float.MAX_VALUE)
    void nextFloat(boolean onlyPositive, float min, float max) {
        assertThat(new PseudoRandom().nextFloat(onlyPositive)).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(bool1 = false, double2 = Double.MIN_VALUE, double3 = Double.MAX_VALUE)
    @TestCase(bool1 = true, double2 = 0, double3 = Double.MAX_VALUE)
    void nextDouble(boolean onlyPositive, double min, double max) {
        assertThat(new PseudoRandom().nextDouble(onlyPositive)).isBetween(min, max);
    }

    @TestWithCases
    @TestCase(int1 = 0, int2 = Integer.MAX_VALUE, int3 = 128)
    @TestCase(int1 = 37, int2 = 52, int3 = 52)
    @TestCase(int1 = 0, int2 = 4, int3 = 4)
    @TestCase(int1 = 1024, int2 = Integer.MAX_VALUE, int3 = 1024+128)
    @TestCase(int1 = 1025, int2 = Integer.MAX_VALUE, int3 = 1025+128)
    @DisplayName("nextString will return a random string with at least min and at most min+128 (or max, whichever is less) characters")
    void nextString(int min, int max, int expectedMaxLen ) {
        var sut = new PseudoRandom();
        var constraints = new StringConstraints(min, max);
        assertThat(sut.nextString(constraints)).isNotBlank();
        assertThat(sut.nextString(constraints).length()).isBetween(min, expectedMaxLen);
    }

    @TestWithCases
    @TestCase(int1 = -3, int2 = 2)
    @TestCase(int1 = 2, int2 = 1)
    @DisplayName("next string will throw exception when called with values that would produce negative length string")
    void nextStringWithIllegalValues(int min, int max) {
        assertThatThrownBy(() -> new PseudoRandom().nextString(new StringConstraints(min,max))).isInstanceOf(SpecimenException.class);
        assertThatThrownBy(() -> new PseudoRandom().nextString(new StringConstraints(min, max))).isInstanceOf(SpecimenException.class);
    }

    @Test
    void nextBool() {
        assertThat(new PseudoRandom().nextBool()).isIn(true, false);
    }

    @Test
    void nextChar() {
        assertThat(new PseudoRandom().nextChar()).isBetween(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    @Test
    void nextByte() {
        assertThat(new PseudoRandom().nextByte()).isBetween(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }
}
