package com.github.nylle.javafixture.generic;

import com.github.nylle.javafixture.extensions.testcases.TestCase;
import com.github.nylle.javafixture.extensions.testcases.TestWithCases;
import com.github.nylle.javafixture.testobjects.ITestGeneric;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.TestObjectWithGenerics;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseEra;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FixtureTypeTest {

    @Test
    void asClass() {
        assertThat(new FixtureType<String>() {}.asClass()).isEqualTo(String.class);
    }

    @Test
    void isParametrized() {
        assertThat(new FixtureType<String>() {}.isParameterized()).isFalse();
        assertThat(new FixtureType<Optional<String>>() {}.isParameterized()).isTrue();
    }

    @Test
    void isParameterizedStatic() throws NoSuchFieldException {

        var type = TestObject.class.getDeclaredField("value").getGenericType();
        var listType = TestObject.class.getDeclaredField("integers").getGenericType();
        var classType = TestObjectWithGenerics.class.getDeclaredField("aClass").getGenericType();
        var genericType = TestObjectWithGenerics.class.getDeclaredField("generic").getGenericType();

        assertThat(FixtureType.isParameterized(null)).isFalse();
        assertThat(FixtureType.isParameterized(type)).isFalse();
        assertThat(FixtureType.isParameterized(listType)).isTrue();
        assertThat(FixtureType.isParameterized(classType)).isTrue();
        assertThat(FixtureType.isParameterized(genericType)).isTrue();
    }

    @Test
    void isCollection() {
        assertThat(new FixtureType<Collection<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<List<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<NavigableSet<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<SortedSet<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<Set<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<Deque<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<BlockingDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<Queue<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<BlockingQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<TransferQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<ArrayList<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<HashSet<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<TreeSet<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<ArrayDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<LinkedBlockingDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<LinkedList<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<LinkedBlockingQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new FixtureType<LinkedTransferQueue<String>>() {}.isCollection()).isTrue();
    }

    @TestWithCases
    @TestCase(class1 = Collection.class)
    @TestCase(class1 = List.class)
    @TestCase(class1 = NavigableSet.class)
    @TestCase(class1 = SortedSet.class)
    @TestCase(class1 = Set.class)
    @TestCase(class1 = Deque.class)
    @TestCase(class1 = BlockingDeque.class)
    @TestCase(class1 = Queue.class)
    @TestCase(class1 = BlockingQueue.class)
    @TestCase(class1 = TransferQueue.class)
    @TestCase(class1 = ArrayList.class)
    @TestCase(class1 = HashSet.class)
    @TestCase(class1 = TreeSet.class)
    @TestCase(class1 = ArrayDeque.class)
    @TestCase(class1 = LinkedBlockingDeque.class)
    @TestCase(class1 = LinkedList.class)
    @TestCase(class1 = LinkedBlockingQueue.class)
    @TestCase(class1 = LinkedTransferQueue.class)
    void isCollectionFromClass(Class<Collection> collectionType) {
        assertThat(FixtureType.fromClass(collectionType).isCollection()).isTrue();
    }

    @Test
    void isMap() {
        assertThat(new FixtureType<Map>(){}.isMap()).isTrue();
        assertThat(new FixtureType<ConcurrentNavigableMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<ConcurrentMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<NavigableMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<SortedMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<TreeMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<ConcurrentSkipListMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<ConcurrentHashMap>(){}.isMap()).isTrue();
        assertThat(new FixtureType<HashMap>(){}.isMap()).isTrue();
    }

    @TestWithCases
    @TestCase(class1 = Map.class)
    @TestCase(class1 = ConcurrentNavigableMap.class)
    @TestCase(class1 = ConcurrentMap.class)
    @TestCase(class1 = NavigableMap.class)
    @TestCase(class1 = SortedMap.class)
    @TestCase(class1 = TreeMap.class)
    @TestCase(class1 = ConcurrentSkipListMap.class)
    @TestCase(class1 = ConcurrentHashMap.class)
    @TestCase(class1 = HashMap.class)
    void isMapFromClass(Class<Map> mapType) {
        assertThat(FixtureType.fromClass(mapType).isMap()).isTrue();
    }

    @Test
    void isBoxed() {
        assertThat(new FixtureType<String>() {}.isBoxed()).isFalse(); // false!
        assertThat(new FixtureType<Byte>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Short>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Integer>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Long>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Float>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Double>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Character>() {}.isBoxed()).isTrue();
        assertThat(new FixtureType<Boolean>() {}.isBoxed()).isTrue();
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = Byte.class, bool2 = true)
    @TestCase(class1 = Short.class, bool2 = true)
    @TestCase(class1 = Integer.class, bool2 = true)
    @TestCase(class1 = Long.class, bool2 = true)
    @TestCase(class1 = Float.class, bool2 = true)
    @TestCase(class1 = Double.class, bool2 = true)
    @TestCase(class1 = Character.class, bool2 = true)
    @TestCase(class1 = Boolean.class, bool2 = true)
    void isBoxedFromClass(Class<?> value, boolean expected) {
        assertThat(FixtureType.fromClass(value).isBoxed()).isEqualTo(expected);
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = byte.class, bool2 = true)
    @TestCase(class1 = short.class, bool2 = true)
    @TestCase(class1 = int.class, bool2 = true)
    @TestCase(class1 = long.class, bool2 = true)
    @TestCase(class1 = float.class, bool2 = true)
    @TestCase(class1 = double.class, bool2 = true)
    @TestCase(class1 = char.class, bool2 = true)
    @TestCase(class1 = boolean.class, bool2 = true)
    void isPrimitive(Class<?> value, boolean expected) {
        assertThat(FixtureType.fromClass(value).isPrimitive()).isEqualTo(expected);
    }

    @Test
    void isEnum() {
        assertThat(FixtureType.fromClass(String.class).isEnum()).isFalse();
        assertThat(FixtureType.fromClass(TestEnum.class).isEnum()).isTrue();
        assertThat(new FixtureType<TestEnum>() {}.isEnum()).isTrue();
    }

    @Test
    void isArray() {
        assertThat(FixtureType.fromClass(List.class).isArray()).isFalse();
        assertThat(FixtureType.fromClass(int[].class).isArray()).isTrue();
        assertThat(new FixtureType<Integer[]>() {}.isArray()).isTrue();
    }

    @Test
    void isInterface() {
        assertThat(FixtureType.fromClass(String.class).isInterface()).isFalse();
        assertThat(FixtureType.fromClass(List.class).isInterface()).isTrue();
        assertThat(new FixtureType<ITestGeneric<String, List<Optional>>>() {}.isInterface()).isTrue();
    }

    @Test
    void isTimeType() {
        assertThat(new FixtureType<String>(){}.isTimeType()).isFalse(); // false
        assertThat(new FixtureType<Duration>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<JapaneseEra>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<MonthDay>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<Period>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<ZoneId>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<ZoneOffset>(){}.isTimeType()).isTrue();
        assertThat(new FixtureType<ZonedDateTime>(){}.isTimeType()).isTrue();
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = Duration.class, bool2 = true)
    @TestCase(class1 = JapaneseEra.class, bool2 = true)
    @TestCase(class1 = MonthDay.class, bool2 = true)
    @TestCase(class1 = Period.class, bool2 = true)
    @TestCase(class1 = ZoneId.class, bool2 = true)
    @TestCase(class1 = ZoneOffset.class, bool2 = true)
    @TestCase(class1 = ZonedDateTime.class, bool2 = true)
    void isTimeTypeFromClass(Class<?> value, boolean expected) {
        assertThat(FixtureType.fromClass(value).isTimeType()).isEqualTo(expected);
    }

    @Test
    void getGenericTypeArgument() {
        var sut = new FixtureType<Map<String, Optional<Integer>>>(){};

        assertThat(sut.getGenericTypeArgument(0)).isEqualTo(String.class);
        assertThat(sut.getGenericTypeArgument(1)).isInstanceOf(ParameterizedType.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArgument(1))).getRawType()).isEqualTo(Optional.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArgument(1))).getActualTypeArguments()[0]).isEqualTo(Integer.class);

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getGenericTypeArgument(0))
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getGenericTypeArguments() {
        var sut = new FixtureType<Map<String, Optional<Integer>>>(){};

        assertThat(sut.getGenericTypeArguments()).hasSize(2);
        assertThat(sut.getGenericTypeArguments()[0]).isEqualTo(String.class);
        assertThat(sut.getGenericTypeArguments()[1]).isInstanceOf(ParameterizedType.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArguments()[1])).getRawType()).isEqualTo(Optional.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArguments()[1])).getActualTypeArguments()[0]).isEqualTo(Integer.class);

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getTypeParameterName() {
        var sut = new FixtureType<TestObjectGeneric<String, Optional<Integer>>>(){};

        assertThat(sut.getTypeParameterName(0)).isEqualTo("T");
        assertThat(sut.getTypeParameterName(1)).isEqualTo("U");

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getTypeParameterNames() {
        var sut = new FixtureType<TestObjectGeneric<String, Optional<Integer>>>(){};

        assertThat(sut.getTypeParameterNames()).hasSize(2);
        assertThat(sut.getTypeParameterNames()[0]).isEqualTo("T");
        assertThat(sut.getTypeParameterNames()[1]).isEqualTo("U");

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getComponentType() {
        assertThat(new FixtureType<int[]>(){}.getComponentType()).isEqualTo(int.class);
        assertThat(new FixtureType<Integer[]>(){}.getComponentType()).isEqualTo(Integer.class);

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getComponentType())
                .withMessageContaining(" is not an array")
                .withNoCause();
    }

    @TestWithCases
    @TestCase(class1 = int[].class, class2 = int.class)
    @TestCase(class1 = Integer[].class, class2 = Integer.class)
    void getComponentTypeFromClass(Class<?> value, Class<?> expected) {
        assertThat(FixtureType.fromClass(value).getComponentType()).isEqualTo(expected);
    }

    @Test
    void getEnumConstants() {
        assertThat(FixtureType.fromClass(TestEnum.class).getEnumConstants().length).isEqualTo(3);
        assertThat(new FixtureType<TestEnum>(){}.getEnumConstants().length).isEqualTo(3);

        assertThatExceptionOfType(FixtureTypeException.class)
                .isThrownBy(() -> FixtureType.fromClass(String.class).getEnumConstants())
                .withMessageContaining(" is not an enum")
                .withNoCause();
    }

    @Test
    void asParameterizedType() {
        assertThat(new FixtureType<Optional<String>>() {}.asParameterizedType().getRawType()).isEqualTo(Optional.class);
        assertThat(new FixtureType<Optional<String>>() {}.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(new FixtureType<Optional<String>>() {}.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @TestWithCases
    @TestCase(class1 = String.class)
    @TestCase(class1 = List.class)
    @TestCase(class1 = Map.class)
    @TestCase(class1 = Optional.class)
    void fromClass(Class<?> type) {
        FixtureType<?> sut = FixtureType.fromClass(type);

        assertThat(sut.isParameterized()).isFalse();
        assertThat(sut.asClass()).isEqualTo(type);
        assertThatExceptionOfType(FixtureTypeException.class).isThrownBy(() -> sut.asParameterizedType())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void fromType() {
        Type type = new FixtureType<Optional<String>>() {}.asParameterizedType();

        var sut = FixtureType.fromClass(type);

        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asClass()).isEqualTo(Optional.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0]).isEqualTo(String.class);
    }

    @Test
    void createForObjectClass() {
        var sut = new FixtureType<String>() {};

        assertThat(sut.asClass()).isEqualTo(String.class);
        assertThat(sut.isParameterized()).isFalse();
        assertThatExceptionOfType(FixtureTypeException.class).isThrownBy(() -> sut.asParameterizedType())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void createForGenericClass() {
        var sut = new FixtureType<Optional<String>>() {};

        assertThat(sut.asClass()).isEqualTo(Optional.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(Optional.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @Test
    void createForListInterface() {
        var sut = new FixtureType<List<String>>() {};

        assertThat(sut.asClass()).isEqualTo(List.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(List.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @Test
    void createForMapInterface() {
        var sut = new FixtureType<Map<String, Integer>>() {};

        assertThat(sut.asClass()).isEqualTo(Map.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(Map.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(2);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
        assertThat(sut.asParameterizedType().getActualTypeArguments()[1].getTypeName()).isEqualTo(Integer.class.getName());
    }
}