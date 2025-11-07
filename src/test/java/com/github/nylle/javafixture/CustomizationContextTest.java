package com.github.nylle.javafixture;

import com.github.nylle.javafixture.annotations.fixture.TestWithFixture;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CustomizationContextTest {

    @Nested
    class NewForField {

        @Test
        void returnsContextWithIgnoredNestedField() {
            var sut = new CustomizationContext(
                    List.of("field", "fieldWith.nestedField", "fieldWith.nestedField2"),
                    Map.of(),
                    false);

            var actual = sut.newForField("fieldWith");

            assertThat(actual.getIgnoredFields()).containsExactly("nestedField", "nestedField2");
        }

        @Test
        void returnsContextWithEmptyIgnoredNestedFieldWhenFieldHasNoNestedFields() {
            var sut = new CustomizationContext(List.of("field"), Map.of(), false);

            var actual = sut.newForField("field");

            assertThat(actual.getIgnoredFields()).isEmpty();
        }

        @TestWithFixture
        void returnsContextWithCustomizedNestedField(Object expected) {
            var sut = new CustomizationContext(
                    List.of(),
                    Map.of(
                            "field", new Object(),
                            "fieldWith.nestedField", expected,
                            "fieldWith.nestedField2", expected),
                    false);

            var actual = sut.newForField("fieldWith");

            assertThat(actual.getCustomFields()).containsExactlyInAnyOrderEntriesOf(Map.of("nestedField", expected, "nestedField2", expected));
        }

        @TestWithFixture
        void returnsContextWithEmptyCustomizedNestedFieldWhenFieldHasNoNestedFields(Object expected) {
            var sut = new CustomizationContext(
                    List.of(),
                    Map.of("field", new Object()),
                    false);

            var actual = sut.newForField("field");

            assertThat(actual.getCustomFields()).isEmpty();
        }

        @TestWithCases
        @TestCase(bool1 = true)
        @TestCase(bool1 = false)
        void useRandomConstructorIsMaintained(boolean useRandomConstructor, @com.github.nylle.javafixture.annotations.testcases.Fixture String fieldName) {
            var sut = new CustomizationContext(List.of(), Map.of(), useRandomConstructor);

            var actual = sut.newForField(fieldName);

            assertThat(actual.useRandomConstructor()).isEqualTo(useRandomConstructor);
        }
    }

    @Test
    void findAllCustomizedFieldNames() {
        var sut = new CustomizationContext(List.of("remove"), Map.of("change", new Object()), false);

        var actual = sut.findAllCustomizedFieldNames().collect(Collectors.toList());

        assertThat(actual).containsExactlyInAnyOrder("remove", "change");
    }
}
