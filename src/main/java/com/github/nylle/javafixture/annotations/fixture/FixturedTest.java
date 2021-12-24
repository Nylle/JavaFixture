package com.github.nylle.javafixture.annotations.fixture;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface FixturedTest {
    int minCollectionSize() default 10;

    int maxCollectionSize() default 10;

    boolean positiveNumbersOnly() default false;
}
