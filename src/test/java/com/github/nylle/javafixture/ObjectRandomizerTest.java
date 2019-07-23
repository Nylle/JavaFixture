package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class ObjectRandomizerTest {

    @Test
    public void randomSimpleObject() {

        final TestDto result = Randomizer.random(TestDto.class);

        assertThat(result.publicField).isInstanceOf(String.class);
        assertThat(result.getHello()).isInstanceOf(String.class);
        assertThat(result.getPrimitive()).isInstanceOf(Integer.class);
        assertThat(result.getInteger()).isInstanceOf(Integer.class);
    }
}