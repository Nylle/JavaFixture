package com.github.nylle.javafixture.parameterized;

import java.util.HashMap;
import java.util.Map;

public class TestCaseMatrix {

    private Map<Class<?>, Object[]> matrix;

    public TestCaseMatrix(TestCase testCase) {
        matrix = new HashMap<>();
        matrix.put(Class.class, new Class[] { testCase.class1(), testCase.class2(), testCase.class3() });
        matrix.put(String.class, new String[] { testCase.str1(), testCase.str2(), testCase.str3() });
        matrix.put(boolean.class, new Boolean[] { testCase.bool1(), testCase.bool2(), testCase.bool3() });
        matrix.put(int.class, new Integer[] { testCase.int1(), testCase.int2(), testCase.int3() });
        matrix.put(float.class, new Float[] { testCase.float1(), testCase.float2(), testCase.float3() });
        matrix.put(long.class, new Long[] { testCase.long1(), testCase.long2(), testCase.long3() });
        matrix.put(short.class, new Short[] { testCase.short1(), testCase.short2(), testCase.short3() });
        matrix.put(double.class, new Double[] { testCase.double1(), testCase.double2(), testCase.double3() });
        matrix.put(char.class, new Character[] { testCase.char1(), testCase.char2(), testCase.char3() });
        matrix.put(byte.class, new Byte[] { testCase.byte1(), testCase.byte2(), testCase.byte3() });
    }

    @SuppressWarnings("unchecked")
    public <T> T getTestCaseValueFor(Class<T> type, int i) {
        return (T) matrix.get(type)[i];
    }

}
