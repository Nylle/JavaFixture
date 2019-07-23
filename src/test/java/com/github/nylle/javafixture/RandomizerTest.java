package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class RandomizerTest {

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

    @Test
    public void randomSimpleObject() {

        final TestDto result = Randomizer.random(TestDto.class);

        assertThat(result.publicField).isInstanceOf(String.class);
        assertThat(result.getHello()).isInstanceOf(String.class);
        assertThat(result.getPrimitive()).isInstanceOf(Integer.class);
        assertThat(result.getInteger()).isInstanceOf(Integer.class);
    }

    @Test
    public void randomStream() {

        final Stream<TestDto> result = Randomizer.randomStreamOf(TestDto.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Stream.class);

        final List<TestDto> resultList = result.collect(Collectors.toList());

        assertThat(resultList.size()).isGreaterThan(0);
        assertThat(resultList.get(0)).isInstanceOf(TestDto.class);
        assertThat(resultList.get(0).publicField).isInstanceOf(String.class);
        assertThat(resultList.get(0).getHello()).isInstanceOf(String.class);
        assertThat(resultList.get(0).getPrimitive()).isInstanceOf(Integer.class);
        assertThat(resultList.get(0).getInteger()).isInstanceOf(Integer.class);
    }

    @Test
    public void randomStreamWithFixedLength() {

        final Stream<TestDto> result = Randomizer.randomStreamOf(3, TestDto.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Stream.class);

        final List<TestDto> resultList = result.collect(Collectors.toList());

        assertThat(resultList.size()).isEqualTo(3);
        assertThat(resultList.get(0)).isInstanceOf(TestDto.class);
        assertThat(resultList.get(0).publicField).isInstanceOf(String.class);
        assertThat(resultList.get(0).getHello()).isInstanceOf(String.class);
        assertThat(resultList.get(0).getPrimitive()).isInstanceOf(Integer.class);
        assertThat(resultList.get(0).getInteger()).isInstanceOf(Integer.class);
    }

}