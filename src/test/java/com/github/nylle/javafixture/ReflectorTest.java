package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

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

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestDto;

public class ReflectorTest {

    @Test
    public void setField() {

        var sut = new Reflector<>(TestDto.class);

        final TestDto instance = new TestDto();
        instance.setHello("old");

        sut.setField(instance, "hello", "new");

        assertThat(instance.getHello()).isEqualTo("new");
    }

    @Test
    public void unsetField() {

        var sut = new Reflector<>(TestDto.class);

        final TestDto instance = new TestDto();
        instance.setHello("old");
        instance.setPrimitive(99);

        sut.unsetField(instance, "hello");
        sut.unsetField(instance, "primitive");

        assertThat(instance.getHello()).isNull();
        assertThat(instance.getPrimitive()).isEqualTo(0);
    }

    @Test
    public void isCollection() {
        assertThat(Reflector.isCollection(List.class)).isTrue();
        assertThat(Reflector.isCollection(NavigableSet.class)).isTrue();
        assertThat(Reflector.isCollection(SortedSet.class)).isTrue();
        assertThat(Reflector.isCollection(Set.class)).isTrue();
        assertThat(Reflector.isCollection(Deque.class)).isTrue();
        assertThat(Reflector.isCollection(BlockingDeque.class)).isTrue();
        assertThat(Reflector.isCollection(Queue.class)).isTrue();
        assertThat(Reflector.isCollection(BlockingQueue.class)).isTrue();
        assertThat(Reflector.isCollection(TransferQueue.class)).isTrue();

        assertThat(Reflector.isCollection(ArrayList.class)).isTrue();
        assertThat(Reflector.isCollection(HashSet.class)).isTrue();
        assertThat(Reflector.isCollection(TreeSet.class)).isTrue();
        assertThat(Reflector.isCollection(ArrayDeque.class)).isTrue();
        assertThat(Reflector.isCollection(LinkedBlockingDeque.class)).isTrue();
        assertThat(Reflector.isCollection(LinkedList.class)).isTrue();
        assertThat(Reflector.isCollection(LinkedBlockingQueue.class)).isTrue();
        assertThat(Reflector.isCollection(LinkedTransferQueue.class)).isTrue();
    }

    @Test
    public void isMap() {
        assertThat(Reflector.isMap(Map.class)).isTrue();
        assertThat(Reflector.isMap(ConcurrentNavigableMap.class)).isTrue();
        assertThat(Reflector.isMap(ConcurrentMap.class)).isTrue();
        assertThat(Reflector.isMap(NavigableMap.class)).isTrue();
        assertThat(Reflector.isMap(SortedMap.class)).isTrue();

        assertThat(Reflector.isMap(TreeMap.class)).isTrue();
        assertThat(Reflector.isMap(ConcurrentSkipListMap.class)).isTrue();
        assertThat(Reflector.isMap(ConcurrentHashMap.class)).isTrue();
        assertThat(Reflector.isMap(HashMap.class)).isTrue();
    }

    @Test
    public void isBoxedOrString() {
        assertThat(Reflector.isBoxedOrString(String.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Double.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Float.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Long.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Integer.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Short.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Character.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Byte.class)).isTrue();
        assertThat(Reflector.isBoxedOrString(Boolean.class)).isTrue();
    }

}