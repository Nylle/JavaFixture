package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestCasesProviderTest {

    TestCasesProvider sut = new TestCasesProvider();

    @Test
    void throwsWhenParameterIsAnnotatedWithBothStrictAndFixture() {
        var parameter = mock(Parameter.class);
        when(parameter.getName()).thenReturn("arg0").thenReturn("arg1");
        when(parameter.getType()).thenReturn((Class) TestEnum.class);
        when(parameter.getAnnotation(Strict.class)).thenReturn(newStrict());
        when(parameter.getAnnotation(Fixture.class)).thenReturn(newFixture());

        var method = mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[] { parameter, parameter });

        var extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.provideArguments(extensionContext))
                .withMessage("Arguments annotated with @Fixture cannot be @Strict: arg0, arg1");
    }

    @Test
    void throwsWhenNonEnumParameterIsAnnotatedWithStrict() {
        var parameter = mock(Parameter.class);
        when(parameter.getName()).thenReturn("arg0").thenReturn("arg1");
        when(parameter.getType()).thenReturn((Class)String.class);
        when(parameter.getAnnotation(Strict.class)).thenReturn(newStrict());
        when(parameter.getAnnotation(Fixture.class)).thenReturn(null);

        var method = mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[] { parameter, parameter });

        var extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));

        assertThatExceptionOfType(TestCaseException.class).isThrownBy(() -> sut.provideArguments(extensionContext))
                .withMessage("Arguments annotated with @Strict must be of type Enum. The following arguments are not: arg0, arg1");
    }

    @Test
    void failsWhenStrictAndNotAllEnumValuesAreCovered() {
        var parameter = mock(Parameter.class);
        when(parameter.getName()).thenReturn("arg0");
        when(parameter.getType()).thenReturn((Class)TestEnum.class);
        when(parameter.getAnnotation(Strict.class)).thenReturn(newStrict());

        var testCases = mock(TestCases.class);
        when(testCases.annotationType()).thenReturn((Class)TestCases.class);
        when(testCases.value()).thenReturn(new TestCase[]{ newTestCase("VALUE1") });

        var method = mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[] { parameter });
        when(method.getDeclaredAnnotations()).thenReturn(new Annotation[] { testCases });

        var extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));

        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> sut.provideArguments(extensionContext))
                .withMessageContaining("@Strict requires all Enum values to be covered by test-cases. Missing values for arg0:")
                .withMessageContaining("  TestEnum.VALUE2")
                .withMessageContaining("  TestEnum.VALUE3");
    }

    private static Fixture newFixture() {
        return new Fixture() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Fixture.class;
            }
        };
    }

    private static Strict newStrict() {
        return new Strict() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Strict.class;
            }
        };
    }

    private static TestCase newTestCase(String str1) {
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
}
