package com.github.nylle.javafixture.specimen;


import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecialSpecimenTest {
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(Configuration.configure());
    }

    @Nested
    @DisplayName("constructor throws exception when called")
    class ConstructorTest {
        @Test
        @DisplayName("with type other than File or URI")
        void onlySpecialType() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(SpecimenType.fromClass(Map.class), context))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + Map.class.getName());
        }

        @Test
        @DisplayName("without type")
        void typeIsRequired() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(null, context))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        @DisplayName("without context")
        void contextIsRequired() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(SpecimenType.fromClass(File.class), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = Object.class, bool2 = false)
    @TestCase(class1 = BigInteger.class, bool2 = true)
    @TestCase(class1 = BigDecimal.class, bool2 = true)
    @TestCase(class1 = File.class, bool2 = true)
    @TestCase(class1 = URI.class, bool2 = true)
    void supportsType(Class<?> type, boolean expected) {
        assertThat(SpecialSpecimen.supportsType(SpecimenType.fromClass(type))).isEqualTo(expected);
    }

    @Test
    @DisplayName("creating two files creates two different files")
    void createFile() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(File.class), context);
        var first = (File) sut.create(noContext(), new Annotation[0]);
        var second = (File) sut.create(noContext(), new Annotation[0]);

        assertThat(first.getAbsolutePath()).isNotEqualTo(second.getAbsolutePath());
    }

    @Test
    @DisplayName("create file w/o context creates file with random name")
    void createFileWithoutContext() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(File.class), context);

        var actual = (File) sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isNotNull();
        assertThat(actual.getAbsolutePath()).isNotEmpty();
    }

    @Test
    @DisplayName("create URI creates URI with host, scheme and random path")
    void craeteURI() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(URI.class), context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(URI.class);
        assertThat(((URI) actual).getScheme()).isEqualTo("https");
        assertThat(((URI) actual).getHost()).isNotNull();
        assertThat(((URI) actual).getPath()).isNotEmpty();
    }

    @Test
    @DisplayName("create BigInteger creates random number with a maximum bit length of 1024")
    void createBigInteger() {

        var sut = new SpecialSpecimen<>(new SpecimenType<BigInteger>() {}, context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(BigInteger.class);
        assertThat(actual.bitLength()).isLessThanOrEqualTo(1024);
    }

    @Test
    @DisplayName("create BigInteger creates non-negative random number when context demands it")
    void createNonNegativeBigInteger() {
        var context = new Context(Configuration.configure().usePositiveNumbersOnly(true));
        var sut = new SpecialSpecimen<>(new SpecimenType<BigInteger>() {}, context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isNotNegative();
    }

    @Test
    @DisplayName("create BigDecimal creates random number")
    void createBigDecimal() {
        var sut = new SpecialSpecimen<>(new SpecimenType<BigDecimal>() {}, context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(BigDecimal.class);
    }

    @Test
    @DisplayName("create BigDecimal acreates non-negative random number when context demands it")
    void createNonNegativeBigDecimal() {
        var context = new Context(Configuration.configure().usePositiveNumbersOnly(true));
        var sut = new SpecialSpecimen<>(new SpecimenType<BigDecimal>() {}, context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isNotNegative();
    }

    @Nested
    class SpecTest {

        @TestWithCases
        @TestCase(class1 = String.class, bool2 = false)
        @TestCase(class1 = Object.class, bool2 = false)
        @TestCase(class1 = BigInteger.class, bool2 = true)
        @TestCase(class1 = BigDecimal.class, bool2 = true)
        @TestCase(class1 = File.class, bool2 = true)
        @TestCase(class1 = URI.class, bool2 = true)
        void supports(Class<?> type, boolean expected) {
            assertThat(new SpecialSpecimen.Spec().supports(SpecimenType.fromClass(type))).isEqualTo(expected);
        }

        @TestWithCases
        @TestCase(class1 = BigInteger.class)
        @TestCase(class1 = BigDecimal.class)
        @TestCase(class1 = File.class)
        @TestCase(class1 = URI.class)
        void createReturnsNewSpecimen(Class<?> type) {
            assertThat(new SpecialSpecimen.Spec().create(SpecimenType.fromClass(type), context, null))
                    .isInstanceOf(SpecialSpecimen.class);
        }

        @TestWithCases
        @TestCase(class1 = String.class)
        @TestCase(class1 = Object.class)
        void createThrows(Class<?> type) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new SpecialSpecimen.Spec().create(SpecimenType.fromClass(type), context, null))
                    .withMessageContaining("type: " + type.getName());
        }
    }
}
