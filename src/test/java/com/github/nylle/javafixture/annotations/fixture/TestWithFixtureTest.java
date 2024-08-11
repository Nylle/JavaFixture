package com.github.nylle.javafixture.annotations.fixture;

import com.github.nylle.javafixture.testobjects.example.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWithFixtureTest {

    @TempDir
    Path tempPath;

    @TestWithFixture
    void injectParameterViaMethodExtension(Contract contract, int intValue, Optional<String> optionalString) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
    }

    @TestWithFixture(minCollectionSize = 11, maxCollectionSize = 11, positiveNumbersOnly = true)
    void configurableFixture(List<Integer> input) {
        assertThat(input).hasSize(11);
        assertThat(input).allMatch(x -> x > 1);
    }

    @TestWithFixture
    @DisplayName("Annotated parameters will work when they are at the end of the list")
    void injectTempDirViaJunit(Integer intValue, @TempDir Path injectedTempDir) {
        assertThat(injectedTempDir).isEqualTo(tempPath);
        assertThat( intValue ).isNotNull();
    }

    @TestWithFixture(minCollectionSize = 3, maxCollectionSize = 9)
    @DisplayName("sets should have at least minCollectionSize and at most MaxCollectionSize elements")
    void setsHaveEnoughElements( Set<Object> sut ) {
        assertThat(sut).hasSizeGreaterThanOrEqualTo(3)
                       .hasSizeLessThanOrEqualTo(9);

    }
}
