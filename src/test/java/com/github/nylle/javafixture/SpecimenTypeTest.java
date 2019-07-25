package com.github.nylle.javafixture;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SpecimenTypeTest {

    @Test
    void forObject() {
        var sut = SpecimenType.forObject(String.class);

        assertThat(sut.getType()).isEqualTo(String.class);
        assertThat(sut.getGenericType()).isNull();
        assertThat(sut.getKeyType()).isNull();
        assertThat(sut.getValueType()).isNull();
    }

    @Test
    void forCollection() {
        var sut = SpecimenType.forCollection(List.class, String.class);

        assertThat(sut.getType()).isEqualTo(List.class);
        assertThat(sut.getGenericType()).isEqualTo(String.class);
        assertThat(sut.getKeyType()).isEqualTo(String.class);
        assertThat(sut.getValueType()).isNull();
    }

    @Test
    void forMap() {
        var sut = SpecimenType.forMap(Map.class, String.class, Integer.class);

        assertThat(sut.getType()).isEqualTo(Map.class);
        assertThat(sut.getGenericType()).isEqualTo(String.class);
        assertThat(sut.getKeyType()).isEqualTo(String.class);
        assertThat(sut.getValueType()).isEqualTo(Integer.class);
    }

    @Test
    void equalsIsTrue() {
        var sut = SpecimenType.forMap(Map.class, String.class, Integer.class);
        assertThat(sut.equals(sut)).isTrue();

        var other = SpecimenType.forMap(Map.class, String.class, Integer.class);
        assertThat(sut.equals(other)).isTrue();
    }

    @Test
    void equalsIsFalse() {
        var sut = SpecimenType.forMap(Map.class, String.class, Integer.class);
        assertThat(sut.equals(null)).isFalse();

        var forObject = SpecimenType.forObject(Map.class);
        assertThat(sut.equals(forObject)).isFalse();

        var forCollection = SpecimenType.forCollection(Map.class, String.class);
        assertThat(sut.equals(forCollection)).isFalse();

        var forMap = SpecimenType.forMap(Map.class, Integer.class, String.class);
        assertThat(sut.equals(forMap)).isFalse();
    }

}