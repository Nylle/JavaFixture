package com.github.nylle.javafixture;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static com.github.nylle.javafixture.Fixture.fixture;
import static org.assertj.core.api.Assertions.assertThat;

class CharsetTest {

    @Test
    void charsetTest() {
        var charset = fixture().create(java.nio.charset.Charset.class);
        assertThat( charset ).isNotNull();
    }


    public static class WithCharSet {
        private Charset charset;
    }
}
