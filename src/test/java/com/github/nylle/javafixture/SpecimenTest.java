package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.complex.Contract;

public class SpecimenTest {

    @Test
    public void xxx() {

        var sut = new Specimen<>(Contract.class);

        assertThat(sut).isInstanceOf(Specimen.class);

    }

}