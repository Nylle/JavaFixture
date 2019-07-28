package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
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

class ReflectionHelperTest {

    @Test
    void setField() {

        var instance = new TestPrimitive();
        instance.setHello("old");

        ReflectionHelper.setField("hello", instance, "new");

        assertThat(instance.getHello()).isEqualTo("new");
    }

    @Test
    void unsetField() {

        var instance = new TestPrimitive();
        instance.setHello("old");
        instance.setPrimitive(99);

        ReflectionHelper.unsetField("hello", instance);
        ReflectionHelper.unsetField("primitive", instance);

        assertThat(instance.getHello()).isNull();
        assertThat(instance.getPrimitive()).isEqualTo(0);
    }

    @Test
    void isBoxedOrString() {
        assertThat(ReflectionHelper.isBoxedOrString(String.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Double.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Float.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Long.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Integer.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Short.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Character.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Byte.class)).isTrue();
        assertThat(ReflectionHelper.isBoxedOrString(Boolean.class)).isTrue();
    }

    @Test
    void isCollection() {
        assertThat(ReflectionHelper.isCollection(List.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(NavigableSet.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(SortedSet.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(Set.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(Deque.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(BlockingDeque.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(Queue.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(BlockingQueue.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(TransferQueue.class)).isTrue();

        assertThat(ReflectionHelper.isCollection(ArrayList.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(HashSet.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(TreeSet.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(ArrayDeque.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(LinkedBlockingDeque.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(LinkedList.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(LinkedBlockingQueue.class)).isTrue();
        assertThat(ReflectionHelper.isCollection(LinkedTransferQueue.class)).isTrue();
    }

    @Test
    void isMap() {
        assertThat(ReflectionHelper.isMap(Map.class)).isTrue();
        assertThat(ReflectionHelper.isMap(ConcurrentNavigableMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(ConcurrentMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(NavigableMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(SortedMap.class)).isTrue();

        assertThat(ReflectionHelper.isMap(TreeMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(ConcurrentSkipListMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(ConcurrentHashMap.class)).isTrue();
        assertThat(ReflectionHelper.isMap(HashMap.class)).isTrue();
    }

    @Test
    void isParameterizedType() throws NoSuchFieldException {

        var type = TestObject.class.getDeclaredField("value").getGenericType();
        var parameterizedType = TestObject.class.getDeclaredField("integers").getGenericType();

        assertThat(ReflectionHelper.isParameterizedType(null)).isFalse();
        assertThat(ReflectionHelper.isParameterizedType(type)).isFalse();
        assertThat(ReflectionHelper.isParameterizedType(parameterizedType)).isTrue();
    }

    @Test
    void getGenericType() throws NoSuchFieldException {

        var type = TestObject.class.getDeclaredField("strings").getGenericType();

        assertThat(ReflectionHelper.getGenericType(type, 0)).isEqualTo(Integer.class);
        assertThat(ReflectionHelper.getGenericType(type, 1)).isEqualTo(String.class);
    }

    @Test
    void isStatic() throws NoSuchFieldException {
        var field = TestObject.class.getDeclaredField("value");
        var staticField = TestObject.class.getDeclaredField("STATIC_FIELD");

        assertThat(ReflectionHelper.isStatic(staticField)).isTrue();
        assertThat(ReflectionHelper.isStatic(field)).isFalse();
    }
}