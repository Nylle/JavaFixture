package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class PrimitiveRandomizerTest {
    @Test
    public void randomPrimitives() {

        assertThat(Randomizer.random(String.class)).isInstanceOf(String.class);

        assertThat(Randomizer.random(Boolean.class)).isInstanceOf(Boolean.class);
        assertThat(Randomizer.random(boolean.class)).isInstanceOf(Boolean.class);

        assertThat(Randomizer.random(Byte.class)).isInstanceOf(Byte.class);
        assertThat(Randomizer.random(byte.class)).isInstanceOf(Byte.class);

        assertThat(Randomizer.random(Short.class)).isInstanceOf(Short.class);
        assertThat(Randomizer.random(short.class)).isInstanceOf(Short.class);

        assertThat(Randomizer.random(Integer.class)).isInstanceOf(Integer.class);
        assertThat(Randomizer.random(int.class)).isInstanceOf(Integer.class);

        assertThat(Randomizer.random(Long.class)).isInstanceOf(Long.class);
        assertThat(Randomizer.random(long.class)).isInstanceOf(Long.class);

        assertThat(Randomizer.random(Float.class)).isInstanceOf(Float.class);
        assertThat(Randomizer.random(float.class)).isInstanceOf(Float.class);

        assertThat(Randomizer.random(Double.class)).isInstanceOf(Double.class);
        assertThat(Randomizer.random(double.class)).isInstanceOf(Double.class);
    }


}