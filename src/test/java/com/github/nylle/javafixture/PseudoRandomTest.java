package com.github.nylle.javafixture;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void nextString() {
        assertThat(new PseudoRandom().nextString()).isNotBlank();
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