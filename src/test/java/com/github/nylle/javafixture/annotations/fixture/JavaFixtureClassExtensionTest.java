package com.github.nylle.javafixture.annotations.fixture;

import com.github.nylle.javafixture.testobjects.example.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(JavaFixtureExtension.class)
class JavaFixtureClassExtensionTest {

    @TempDir
    Path tempPath;

    @FixturedTest
    void injectParameterViaClassExtension(Contract contract, int intValue, Optional<String> optionalString) {
        assertThat(contract.getId()).isBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        assertThat(intValue).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(optionalString).isInstanceOf(Optional.class);
    }

    @FixturedTest
    void injectTempDirViaJunit(@TempDir Path injectedTempDir, Integer intValue) {
        assertThat(injectedTempDir).isEqualTo(tempPath);
        assertThat(intValue).isNotNull();
    }

    @FixturedTest(positiveNumbersOnly = true, minCollectionSize = 2, maxCollectionSize = 3)
    void useAnnotationProperties(Long positiveValue, List<Integer> collection) {
        assertThat(positiveValue).isPositive();
        assertThat(collection.size()).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(3);
        collection.forEach(member -> assertThat(member).isPositive());
    }

    @FixturedTest(minCollectionSize = 2, maxCollectionSize = 2)
    void setsShouldContainEnoughElements(Set<Object> sut) {
        assertThat(sut).hasSize(2);
    }

    @FixturedTest
    void withoutParametersItIsJustAnotherTest() {
        assertThat(true).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("you can mix @FixturedTest with other tests in the same class")
    void ignoreExtensionwhenTestIsNotFixtureTest(int fromJunit) {
        assertThat(fromJunit).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(2);
    }
}
