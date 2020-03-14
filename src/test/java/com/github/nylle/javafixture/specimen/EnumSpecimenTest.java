package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.generic.FixtureType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumSpecimenTest {

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new EnumSpecimen<>(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void onlyEnumTypes() {
        assertThatThrownBy(() -> new EnumSpecimen<>(FixtureType.fromClass(Object.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Object.class.getName());
    }

    @Test
    void createEnum() {
        var sut = new EnumSpecimen<>(FixtureType.fromClass(TestEnum.class));

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestEnum.class);
        assertThat(actual.toString()).isIn("VALUE1", "VALUE2", "VALUE3");
    }
}