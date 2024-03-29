package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.annotations.fixture.JavaFixtureProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JavaFixtureProvider.class)
@ParameterizedTest
public @interface Fixture {
}
