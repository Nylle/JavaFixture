package com.github.nylle.javafixture.annotations.fixture;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JavaFixtureProvider.class)
@ParameterizedTest
public @interface TestWithFixture {
    int minCollectionSize() default 10;

    int maxCollectionSize() default 10;

    boolean positiveNumbersOnly() default false;
}
