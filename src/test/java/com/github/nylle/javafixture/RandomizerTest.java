package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class RandomizerTest {

    @Test
    public void randomPrimitives() {

        var sut = new Randomizer();

        assertThat(sut.random(String.class)).isInstanceOf(String.class);

        assertThat(sut.random(Boolean.class)).isInstanceOf(Boolean.class);
        assertThat(sut.random(boolean.class)).isInstanceOf(Boolean.class);

        assertThat(sut.random(Byte.class)).isInstanceOf(Byte.class);
        assertThat(sut.random(byte.class)).isInstanceOf(Byte.class);

        assertThat(sut.random(Short.class)).isInstanceOf(Short.class);
        assertThat(sut.random(short.class)).isInstanceOf(Short.class);

        assertThat(sut.random(Integer.class)).isInstanceOf(Integer.class);
        assertThat(sut.random(int.class)).isInstanceOf(Integer.class);

        assertThat(sut.random(Long.class)).isInstanceOf(Long.class);
        assertThat(sut.random(long.class)).isInstanceOf(Long.class);

        assertThat(sut.random(Float.class)).isInstanceOf(Float.class);
        assertThat(sut.random(float.class)).isInstanceOf(Float.class);

        assertThat(sut.random(Double.class)).isInstanceOf(Double.class);
        assertThat(sut.random(double.class)).isInstanceOf(Double.class);
    }

    @Test
    public void randomSimpleObject() {
        var sut = new Randomizer();

        final TestDto result = sut.random(TestDto.class);

        assertThat(result.publicField).isInstanceOf(String.class);
        assertThat(result.getHello()).isInstanceOf(String.class);
        assertThat(result.getPrimitive()).isInstanceOf(Integer.class);
        assertThat(result.getInteger()).isInstanceOf(Integer.class);
    }

    @Test
    public void randomInterface() {
        var sut = new Randomizer();

        final TestInterface result = sut.random(TestInterface.class);

        assertThat(result.getTestDto()).isInstanceOf(TestDto.class);
        assertThat(result.toString()).isInstanceOf(String.class);
        assertThat(result.publicField).isEqualTo(1);
    }

    interface TestInterface {
        int publicField = 1;

        TestDto getTestDto();

        void setTestDto(TestDto value);

        String toString();
    }
}