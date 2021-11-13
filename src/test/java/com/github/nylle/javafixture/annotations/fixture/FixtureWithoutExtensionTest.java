package com.github.nylle.javafixture.annotations.fixture;

import static org.assertj.core.api.Assertions.assertThat;

public class FixtureWithoutExtensionTest {
    @FixturedTest
    void withoutParametersThisIsJustAnotherTest() {
        assertThat(true).isTrue();
    }
}
