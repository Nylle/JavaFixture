package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class RandomizerTest {

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