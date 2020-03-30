package com.github.nylle.javafixture;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.testobjects.ITestGeneric;
import com.github.nylle.javafixture.testobjects.TestAbstractClass;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.TestObjectWithGenerics;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
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

class SpecimenTypeTest {

    @Test
    void asClass() {
        assertThat(new SpecimenType<String>() {}.asClass()).isEqualTo(String.class);
    }

    @Test
    void isParametrized() {
        assertThat(new SpecimenType<String>() {}.isParameterized()).isFalse();
        assertThat(new SpecimenType<Optional<String>>() {}.isParameterized()).isTrue();
    }

    @Test
    void isParameterizedStatic() throws NoSuchFieldException {

        var type = TestObject.class.getDeclaredField("value").getGenericType();
        var listType = TestObject.class.getDeclaredField("integers").getGenericType();
        var classType = TestObjectWithGenerics.class.getDeclaredField("aClass").getGenericType();
        var genericType = TestObjectWithGenerics.class.getDeclaredField("generic").getGenericType();

        assertThat(SpecimenType.isParameterized(null)).isFalse();
        assertThat(SpecimenType.isParameterized(type)).isFalse();
        assertThat(SpecimenType.isParameterized(listType)).isTrue();
        assertThat(SpecimenType.isParameterized(classType)).isTrue();
        assertThat(SpecimenType.isParameterized(genericType)).isTrue();
    }

    @Test
    void isCollection() {
        assertThat(new SpecimenType<Collection<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<List<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<NavigableSet<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<SortedSet<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<Set<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<Deque<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<BlockingDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<Queue<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<BlockingQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<TransferQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<ArrayList<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<HashSet<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<TreeSet<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<ArrayDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<LinkedBlockingDeque<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<LinkedList<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<LinkedBlockingQueue<String>>() {}.isCollection()).isTrue();
        assertThat(new SpecimenType<LinkedTransferQueue<String>>() {}.isCollection()).isTrue();
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
        assertThat(SpecimenType.fromClass(collectionType).isCollection()).isTrue();
    }

    @Test
    void isMap() {
        assertThat(new SpecimenType<Map>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<ConcurrentNavigableMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<ConcurrentMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<NavigableMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<SortedMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<TreeMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<ConcurrentSkipListMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<ConcurrentHashMap>(){}.isMap()).isTrue();
        assertThat(new SpecimenType<HashMap>(){}.isMap()).isTrue();
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
        assertThat(SpecimenType.fromClass(mapType).isMap()).isTrue();
    }

    @Test
    void isBoxed() {
        assertThat(new SpecimenType<String>() {}.isBoxed()).isFalse(); // false!
        assertThat(new SpecimenType<Byte>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Short>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Integer>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Long>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Float>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Double>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Character>() {}.isBoxed()).isTrue();
        assertThat(new SpecimenType<Boolean>() {}.isBoxed()).isTrue();
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
        assertThat(SpecimenType.fromClass(value).isBoxed()).isEqualTo(expected);
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
        assertThat(SpecimenType.fromClass(value).isPrimitive()).isEqualTo(expected);
    }

    @Test
    void isEnum() {
        assertThat(SpecimenType.fromClass(String.class).isEnum()).isFalse();
        assertThat(SpecimenType.fromClass(TestEnum.class).isEnum()).isTrue();
        assertThat(new SpecimenType<TestEnum>() {}.isEnum()).isTrue();
    }

    @Test
    void isArray() {
        assertThat(SpecimenType.fromClass(List.class).isArray()).isFalse();
        assertThat(SpecimenType.fromClass(int[].class).isArray()).isTrue();
        assertThat(new SpecimenType<Integer[]>() {}.isArray()).isTrue();
    }

    @Test
    void isInterface() {
        assertThat(SpecimenType.fromClass(String.class).isInterface()).isFalse();
        assertThat(SpecimenType.fromClass(List.class).isInterface()).isTrue();
        assertThat(new SpecimenType<ITestGeneric<String, List<Optional>>>() {}.isInterface()).isTrue();
    }

    @Test
    void isAbstract() {
        assertThat(SpecimenType.fromClass(TestObject.class).isAbstract()).isFalse();
        assertThat(SpecimenType.fromClass(TestAbstractClass.class).isAbstract()).isTrue();
    }

    @Test
    void isTimeType() {
        assertThat(new SpecimenType<String>(){}.isTimeType()).isFalse(); // false
        assertThat(new SpecimenType<Duration>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<JapaneseEra>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<MonthDay>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<Period>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<ZoneId>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<ZoneOffset>(){}.isTimeType()).isTrue();
        assertThat(new SpecimenType<ZonedDateTime>(){}.isTimeType()).isTrue();
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
    @TestCase(class1 = Instant.class, bool2 = true)
    @TestCase(class1 = LocalDate.class, bool2 = true)
    @TestCase(class1 = java.util.Date.class, bool2 = true)
    @TestCase(class1 = java.sql.Date.class, bool2 = true)
    void isTimeTypeFromClass(Class<?> value, boolean expected) {
        assertThat(SpecimenType.fromClass(value).isTimeType()).isEqualTo(expected);
    }

    @Test
    void getGenericTypeArgument() {
        var sut = new SpecimenType<Map<String, Optional<Integer>>>(){};

        assertThat(sut.getGenericTypeArgument(0)).isEqualTo(String.class);
        assertThat(sut.getGenericTypeArgument(1)).isInstanceOf(ParameterizedType.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArgument(1))).getRawType()).isEqualTo(Optional.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArgument(1))).getActualTypeArguments()[0]).isEqualTo(Integer.class);

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getGenericTypeArgument(0))
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getGenericTypeArguments() {
        var sut = new SpecimenType<Map<String, Optional<Integer>>>(){};

        assertThat(sut.getGenericTypeArguments()).hasSize(2);
        assertThat(sut.getGenericTypeArguments()[0]).isEqualTo(String.class);
        assertThat(sut.getGenericTypeArguments()[1]).isInstanceOf(ParameterizedType.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArguments()[1])).getRawType()).isEqualTo(Optional.class);
        assertThat(((ParameterizedType)(sut.getGenericTypeArguments()[1])).getActualTypeArguments()[0]).isEqualTo(Integer.class);

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getTypeParameterName() {
        var sut = new SpecimenType<TestObjectGeneric<String, Optional<Integer>>>(){};

        assertThat(sut.getTypeParameterName(0)).isEqualTo("T");
        assertThat(sut.getTypeParameterName(1)).isEqualTo("U");

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getTypeParameterNames() {
        var sut = new SpecimenType<TestObjectGeneric<String, Optional<Integer>>>(){};

        assertThat(sut.getTypeParameterNames()).hasSize(2);
        assertThat(sut.getTypeParameterNames()[0]).isEqualTo("T");
        assertThat(sut.getTypeParameterNames()[1]).isEqualTo("U");

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getGenericTypeArguments())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void getComponentType() {
        assertThat(new SpecimenType<int[]>(){}.getComponentType()).isEqualTo(int.class);
        assertThat(new SpecimenType<Integer[]>(){}.getComponentType()).isEqualTo(Integer.class);

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getComponentType())
                .withMessageContaining(" is not an array")
                .withNoCause();
    }

    @TestWithCases
    @TestCase(class1 = int[].class, class2 = int.class)
    @TestCase(class1 = Integer[].class, class2 = Integer.class)
    void getComponentTypeFromClass(Class<?> value, Class<?> expected) {
        assertThat(SpecimenType.fromClass(value).getComponentType()).isEqualTo(expected);
    }

    @Test
    void getEnumConstants() {
        assertThat(SpecimenType.fromClass(TestEnum.class).getEnumConstants().length).isEqualTo(3);
        assertThat(new SpecimenType<TestEnum>(){}.getEnumConstants().length).isEqualTo(3);

        assertThatExceptionOfType(SpecimenTypeException.class)
                .isThrownBy(() -> SpecimenType.fromClass(String.class).getEnumConstants())
                .withMessageContaining(" is not an enum")
                .withNoCause();
    }

    @Test
    void getName() {
        assertThat(SpecimenType.fromClass(Optional.class).getName()).isEqualTo("java.util.Optional");
        assertThat(new SpecimenType<Optional<String>>(){}.getName()).isEqualTo("java.util.Optional<java.lang.String>");
    }

    @Test
    void asParameterizedType() {
        assertThat(new SpecimenType<Optional<String>>() {}.asParameterizedType().getRawType()).isEqualTo(Optional.class);
        assertThat(new SpecimenType<Optional<String>>() {}.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(new SpecimenType<Optional<String>>() {}.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @TestWithCases
    @TestCase(class1 = String.class)
    @TestCase(class1 = List.class)
    @TestCase(class1 = Map.class)
    @TestCase(class1 = Optional.class)
    void fromClass(Class<?> type) {
        SpecimenType<?> sut = SpecimenType.fromClass(type);

        assertThat(sut.isParameterized()).isFalse();
        assertThat(sut.asClass()).isEqualTo(type);
        assertThatExceptionOfType(SpecimenTypeException.class).isThrownBy(() -> sut.asParameterizedType())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void fromType() {
        Type type = new SpecimenType<Optional<String>>() {}.asParameterizedType();

        var sut = SpecimenType.fromClass(type);

        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asClass()).isEqualTo(Optional.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0]).isEqualTo(String.class);
    }

    @Test
    void createForObjectClass() {
        var sut = new SpecimenType<String>() {};

        assertThat(sut.asClass()).isEqualTo(String.class);
        assertThat(sut.isParameterized()).isFalse();
        assertThatExceptionOfType(SpecimenTypeException.class).isThrownBy(() -> sut.asParameterizedType())
                .withMessageContaining(" is not a ParameterizedType")
                .withNoCause();
    }

    @Test
    void createForGenericClass() {
        var sut = new SpecimenType<Optional<String>>() {};

        assertThat(sut.asClass()).isEqualTo(Optional.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(Optional.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @Test
    void createForListInterface() {
        var sut = new SpecimenType<List<String>>() {};

        assertThat(sut.asClass()).isEqualTo(List.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(List.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(1);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
    }

    @Test
    void createForMapInterface() {
        var sut = new SpecimenType<Map<String, Integer>>() {};

        assertThat(sut.asClass()).isEqualTo(Map.class);
        assertThat(sut.isParameterized()).isTrue();
        assertThat(sut.asParameterizedType().getRawType()).isEqualTo(Map.class);
        assertThat(sut.asParameterizedType().getActualTypeArguments().length).isEqualTo(2);
        assertThat(sut.asParameterizedType().getActualTypeArguments()[0].getTypeName()).isEqualTo(String.class.getName());
        assertThat(sut.asParameterizedType().getActualTypeArguments()[1].getTypeName()).isEqualTo(Integer.class.getName());
    }
}