package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class InterfaceRandomizerTest {

    @Test
    public void randomInterface() {
        final TestInterface result = Randomizer.random(TestInterface.class);
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