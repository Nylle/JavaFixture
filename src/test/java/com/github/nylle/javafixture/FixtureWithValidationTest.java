package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithJakartaValidationAnnotations;
import com.github.nylle.javafixture.testobjects.TestObjectWithJavaxValidationAnnotations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FixtureWithValidationTest {

    @Test
    void javaxSizeAnnotationIsSupported() {
        var sut = new Fixture().create(TestObjectWithJavaxValidationAnnotations.class);

        assertThat(sut.getWithMinAnnotation().length()).isGreaterThanOrEqualTo(5);
        assertThat(sut.getWithMaxAnnotation().length()).isLessThanOrEqualTo(100);
        assertThat(sut.getWithMinMaxAnnotation().length()).isBetween(3,6);
    }

    @Test
    void jakartaSizeAnnotationIsSupported() {
        var sut = new Fixture().create(TestObjectWithJakartaValidationAnnotations.class);

        assertThat(sut.getWithMinAnnotation().length()).isGreaterThanOrEqualTo(5);
        assertThat(sut.getWithMaxAnnotation().length()).isLessThanOrEqualTo(100);
        assertThat(sut.getWithMinMaxAnnotation().length()).isBetween(3,6);
    }
}
