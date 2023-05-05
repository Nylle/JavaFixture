package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReflectedTestCaseTest {

    @Test
    void getTestCaseValueFor_ThrowsIfTwoDifferentValuesAreCustomized() {
        var testCaseWithDuplicateCustomizationsAt_1_2_3_4_6 = createCustomTestCase(
                "foo", true,
                2.0f, 2L,
                TestObject.class, 3,
                (short) 4, '4',
                (byte) 6, 6.0d);

        var sut = new ReflectedTestCase(testCaseWithDuplicateCustomizationsAt_1_2_3_4_6);

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(String.class, 0))
                .withMessageContaining("Duplicate customisation found for argument at position 1, wanted: java.lang.String, found: ")
                .withMessageContaining("java.lang.Boolean")
                .withMessageContaining("java.lang.String");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Boolean.class, 0))
                .withMessageContaining("Duplicate customisation found for argument at position 1, wanted: java.lang.Boolean, found: ")
                .withMessageContaining("java.lang.Boolean")
                .withMessageContaining("java.lang.String");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Float.class, 1))
                .withMessageContaining("Duplicate customisation found for argument at position 2, wanted: java.lang.Float, found: ")
                .withMessageContaining("java.lang.Float")
                .withMessageContaining("java.lang.Long");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Long.class, 1))
                .withMessageContaining("Duplicate customisation found for argument at position 2, wanted: java.lang.Long, found: ")
                .withMessageContaining("java.lang.Float")
                .withMessageContaining("java.lang.Long");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(TestObject.class.getClass(), 2))
                .withMessageContaining("Duplicate customisation found for argument at position 3, wanted: java.lang.Class, found: ")
                .withMessageContaining("java.lang.Class")
                .withMessageContaining("java.lang.Integer");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Integer.class, 2))
                .withMessageContaining("Duplicate customisation found for argument at position 3, wanted: java.lang.Integer, found: ")
                .withMessageContaining("java.lang.Class")
                .withMessageContaining("java.lang.Integer");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Short.class, 3))
                .withMessageContaining("Duplicate customisation found for argument at position 4, wanted: java.lang.Short, found: ")
                .withMessageContaining("java.lang.Short")
                .withMessageContaining("java.lang.Character");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Character.class, 3))
                .withMessageContaining("Duplicate customisation found for argument at position 4, wanted: java.lang.Character, found: ")
                .withMessageContaining("java.lang.Short")
                .withMessageContaining("java.lang.Character");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Byte.class, 5))
                .withMessageContaining("Duplicate customisation found for argument at position 6, wanted: java.lang.Byte, found: ")
                .withMessageContaining("java.lang.Byte")
                .withMessageContaining("java.lang.Double");

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.getTestCaseValueFor(Double.class, 5))
                .withMessageContaining("Duplicate customisation found for argument at position 6, wanted: java.lang.Double, found: ")
                .withMessageContaining("java.lang.Byte")
                .withMessageContaining("java.lang.Double");
    }

    @Test
    void createsReflectedTestCaseWithDefaultValues() {

        var actual = new ReflectedTestCase(createDefaultTestCase());

        assertThat(actual.getTestCaseValueFor(Class.class, 0)).isEqualTo(Object.class);
        assertThat(actual.getTestCaseValueFor(Class.class, 1)).isEqualTo(Object.class);
        assertThat(actual.getTestCaseValueFor(Class.class, 2)).isEqualTo(Object.class);
        assertThat(actual.getTestCaseValueFor(Class.class, 3)).isEqualTo(Object.class);
        assertThat(actual.getTestCaseValueFor(Class.class, 4)).isEqualTo(Object.class);
        assertThat(actual.getTestCaseValueFor(Class.class, 5)).isEqualTo(Object.class);

        assertThat(actual.getTestCaseValueFor(String.class, 0)).isEmpty();
        assertThat(actual.getTestCaseValueFor(String.class, 1)).isEmpty();
        assertThat(actual.getTestCaseValueFor(String.class, 2)).isEmpty();
        assertThat(actual.getTestCaseValueFor(String.class, 3)).isEmpty();
        assertThat(actual.getTestCaseValueFor(String.class, 4)).isEmpty();
        assertThat(actual.getTestCaseValueFor(String.class, 5)).isEmpty();

        assertThat(actual.getTestCaseValueFor(Boolean.class, 0)).isFalse();
        assertThat(actual.getTestCaseValueFor(Boolean.class, 1)).isFalse();
        assertThat(actual.getTestCaseValueFor(Boolean.class, 2)).isFalse();
        assertThat(actual.getTestCaseValueFor(Boolean.class, 3)).isFalse();
        assertThat(actual.getTestCaseValueFor(Boolean.class, 4)).isFalse();
        assertThat(actual.getTestCaseValueFor(Boolean.class, 5)).isFalse();

        assertThat(actual.getTestCaseValueFor(Integer.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Integer.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Integer.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Integer.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Integer.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Integer.class, 5)).isZero();

        assertThat(actual.getTestCaseValueFor(Float.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Float.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Float.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Float.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Float.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Float.class, 5)).isZero();

        assertThat(actual.getTestCaseValueFor(Long.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Long.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Long.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Long.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Long.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Long.class, 5)).isZero();

        assertThat(actual.getTestCaseValueFor(Short.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Short.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Short.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Short.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Short.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Short.class, 5)).isZero();

        assertThat(actual.getTestCaseValueFor(Double.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Double.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Double.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Double.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Double.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Double.class, 5)).isZero();

        assertThat(actual.getTestCaseValueFor(Character.class, 0)).isEqualTo(Character.MIN_VALUE);
        assertThat(actual.getTestCaseValueFor(Character.class, 1)).isEqualTo(Character.MIN_VALUE);
        assertThat(actual.getTestCaseValueFor(Character.class, 2)).isEqualTo(Character.MIN_VALUE);
        assertThat(actual.getTestCaseValueFor(Character.class, 3)).isEqualTo(Character.MIN_VALUE);
        assertThat(actual.getTestCaseValueFor(Character.class, 4)).isEqualTo(Character.MIN_VALUE);
        assertThat(actual.getTestCaseValueFor(Character.class, 5)).isEqualTo(Character.MIN_VALUE);

        assertThat(actual.getTestCaseValueFor(Byte.class, 0)).isZero();
        assertThat(actual.getTestCaseValueFor(Byte.class, 1)).isZero();
        assertThat(actual.getTestCaseValueFor(Byte.class, 2)).isZero();
        assertThat(actual.getTestCaseValueFor(Byte.class, 3)).isZero();
        assertThat(actual.getTestCaseValueFor(Byte.class, 4)).isZero();
        assertThat(actual.getTestCaseValueFor(Byte.class, 5)).isZero();
    }

    private static TestCase createDefaultTestCase() {
        return new TestCase() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public Class class1() {
                return Object.class;
            }

            @Override
            public Class class2() {
                return Object.class;
            }

            @Override
            public Class class3() {
                return Object.class;
            }

            @Override
            public Class class4() {
                return Object.class;
            }

            @Override
            public Class class5() {
                return Object.class;
            }

            @Override
            public Class class6() {
                return Object.class;
            }

            @Override
            public String str1() {
                return "";
            }

            @Override
            public String str2() {
                return "";
            }

            @Override
            public String str3() {
                return "";
            }

            @Override
            public String str4() {
                return "";
            }

            @Override
            public String str5() {
                return "";
            }

            @Override
            public String str6() {
                return "";
            }

            @Override
            public boolean bool1() {
                return false;
            }

            @Override
            public boolean bool2() {
                return false;
            }

            @Override
            public boolean bool3() {
                return false;
            }

            @Override
            public boolean bool4() {
                return false;
            }

            @Override
            public boolean bool5() {
                return false;
            }

            @Override
            public boolean bool6() {
                return false;
            }

            @Override
            public int int1() {
                return 0;
            }

            @Override
            public int int2() {
                return 0;
            }

            @Override
            public int int3() {
                return 0;
            }

            @Override
            public int int4() {
                return 0;
            }

            @Override
            public int int5() {
                return 0;
            }

            @Override
            public int int6() {
                return 0;
            }

            @Override
            public float float1() {
                return 0;
            }

            @Override
            public float float2() {
                return 0;
            }

            @Override
            public float float3() {
                return 0;
            }

            @Override
            public float float4() {
                return 0;
            }

            @Override
            public float float5() {
                return 0;
            }

            @Override
            public float float6() {
                return 0;
            }

            @Override
            public long long1() {
                return 0;
            }

            @Override
            public long long2() {
                return 0;
            }

            @Override
            public long long3() {
                return 0;
            }

            @Override
            public long long4() {
                return 0;
            }

            @Override
            public long long5() {
                return 0;
            }

            @Override
            public long long6() {
                return 0;
            }

            @Override
            public short short1() {
                return 0;
            }

            @Override
            public short short2() {
                return 0;
            }

            @Override
            public short short3() {
                return 0;
            }

            @Override
            public short short4() {
                return 0;
            }

            @Override
            public short short5() {
                return 0;
            }

            @Override
            public short short6() {
                return 0;
            }

            @Override
            public double double1() {
                return 0;
            }

            @Override
            public double double2() {
                return 0;
            }

            @Override
            public double double3() {
                return 0;
            }

            @Override
            public double double4() {
                return 0;
            }

            @Override
            public double double5() {
                return 0;
            }

            @Override
            public double double6() {
                return 0;
            }

            @Override
            public char char1() {
                return 0;
            }

            @Override
            public char char2() {
                return 0;
            }

            @Override
            public char char3() {
                return 0;
            }

            @Override
            public char char4() {
                return 0;
            }

            @Override
            public char char5() {
                return 0;
            }

            @Override
            public char char6() {
                return 0;
            }

            @Override
            public byte byte1() {
                return 0;
            }

            @Override
            public byte byte2() {
                return 0;
            }

            @Override
            public byte byte3() {
                return 0;
            }

            @Override
            public byte byte4() {
                return 0;
            }

            @Override
            public byte byte5() {
                return 0;
            }

            @Override
            public byte byte6() {
                return 0;
            }
        };
    }

    private static TestCase createCustomTestCase(String str1, boolean bool1, float float2, long long2, Class class3, int int3, short short4, char char4, byte byte6, double double6) {
        return new TestCase() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public Class class1() {
                return Object.class;
            }

            @Override
            public Class class2() {
                return Object.class;
            }

            @Override
            public Class class3() {
                return class3;
            }

            @Override
            public Class class4() {
                return Object.class;
            }

            @Override
            public Class class5() {
                return Object.class;
            }

            @Override
            public Class class6() {
                return Object.class;
            }

            @Override
            public String str1() {
                return str1;
            }

            @Override
            public String str2() {
                return "";
            }

            @Override
            public String str3() {
                return "";
            }

            @Override
            public String str4() {
                return "";
            }

            @Override
            public String str5() {
                return "";
            }

            @Override
            public String str6() {
                return "";
            }

            @Override
            public boolean bool1() {
                return bool1;
            }

            @Override
            public boolean bool2() {
                return false;
            }

            @Override
            public boolean bool3() {
                return false;
            }

            @Override
            public boolean bool4() {
                return false;
            }

            @Override
            public boolean bool5() {
                return false;
            }

            @Override
            public boolean bool6() {
                return false;
            }

            @Override
            public int int1() {
                return 0;
            }

            @Override
            public int int2() {
                return 0;
            }

            @Override
            public int int3() {
                return int3;
            }

            @Override
            public int int4() {
                return 0;
            }

            @Override
            public int int5() {
                return 0;
            }

            @Override
            public int int6() {
                return 0;
            }

            @Override
            public float float1() {
                return 0;
            }

            @Override
            public float float2() {
                return float2;
            }

            @Override
            public float float3() {
                return 0;
            }

            @Override
            public float float4() {
                return 0;
            }

            @Override
            public float float5() {
                return 0;
            }

            @Override
            public float float6() {
                return 0;
            }

            @Override
            public long long1() {
                return 0;
            }

            @Override
            public long long2() {
                return long2;
            }

            @Override
            public long long3() {
                return 0;
            }

            @Override
            public long long4() {
                return 0;
            }

            @Override
            public long long5() {
                return 0;
            }

            @Override
            public long long6() {
                return 0;
            }

            @Override
            public short short1() {
                return 0;
            }

            @Override
            public short short2() {
                return 0;
            }

            @Override
            public short short3() {
                return 0;
            }

            @Override
            public short short4() {
                return short4;
            }

            @Override
            public short short5() {
                return 0;
            }

            @Override
            public short short6() {
                return 0;
            }

            @Override
            public double double1() {
                return 0;
            }

            @Override
            public double double2() {
                return 0;
            }

            @Override
            public double double3() {
                return 0;
            }

            @Override
            public double double4() {
                return 0;
            }

            @Override
            public double double5() {
                return 0;
            }

            @Override
            public double double6() {
                return double6;
            }

            @Override
            public char char1() {
                return 0;
            }

            @Override
            public char char2() {
                return 0;
            }

            @Override
            public char char3() {
                return 0;
            }

            @Override
            public char char4() {
                return char4;
            }

            @Override
            public char char5() {
                return 0;
            }

            @Override
            public char char6() {
                return 0;
            }

            @Override
            public byte byte1() {
                return 0;
            }

            @Override
            public byte byte2() {
                return 0;
            }

            @Override
            public byte byte3() {
                return 0;
            }

            @Override
            public byte byte4() {
                return 0;
            }

            @Override
            public byte byte5() {
                return 0;
            }

            @Override
            public byte byte6() {
                return byte6;
            }
        };
    }
}
