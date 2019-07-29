package com.github.nylle.javafixture.parameterized;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TestCases.class)
public @interface TestCase {
    String str1() default "";
    String str2() default "";
    String str3() default "";
    boolean bool1() default false;
    boolean bool2() default false;
    boolean bool3() default false;
    int int1() default 0;
    int int2() default 0;
    int int3() default 0;
    float float1() default 0.0f;
    float float2() default 0.0f;
    float float3() default 0.0f;
    long long1() default 0L;
    long long2() default 0L;
    long long3() default 0L;
    short short1() default 0;
    short short2() default 0;
    short short3() default 0;
    double double1() default 0.0d;
    double double2() default 0.0d;
    double double3() default 0.0d;
    char char1() default '\u0000';
    char char2() default '\u0000';
    char char3() default '\u0000';
    byte byte1() default 0;
    byte byte2() default 0;
    byte byte3() default 0;
    Class class1() default Object.class;
    Class class2() default Object.class;
    Class class3() default Object.class;
}

