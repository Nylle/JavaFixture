package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestCollections;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import com.github.nylle.javafixture.testobjects.TestInterface;

public class RandomizerTest {

    private static int MIN_SIZE = 2;
    private static int MAX_SIZE = 10;

    @Test
    public void randomPrimitives() {

        var sut = new Randomizer();

        assertThat(sut.random(String.class)).isInstanceOf(String.class);

        assertThat(sut.random(Boolean.class)).isInstanceOf(Boolean.class);
        assertThat(sut.random(boolean.class)).isInstanceOf(Boolean.class);

        assertThat(sut.random(Byte.class)).isInstanceOf(Byte.class);
        assertThat(sut.random(byte.class)).isInstanceOf(Byte.class);

        assertThat(sut.random(Short.class)).isInstanceOf(Short.class);
        assertThat(sut.random(short.class)).isInstanceOf(Short.class);

        assertThat(sut.random(Integer.class)).isInstanceOf(Integer.class);
        assertThat(sut.random(int.class)).isInstanceOf(Integer.class);

        assertThat(sut.random(Long.class)).isInstanceOf(Long.class);
        assertThat(sut.random(long.class)).isInstanceOf(Long.class);

        assertThat(sut.random(Float.class)).isInstanceOf(Float.class);
        assertThat(sut.random(float.class)).isInstanceOf(Float.class);

        assertThat(sut.random(Double.class)).isInstanceOf(Double.class);
        assertThat(sut.random(double.class)).isInstanceOf(Double.class);
    }

    @Test
    public void randomSimpleObject() {
        var sut = new Randomizer();

        final TestPrimitive result = sut.random(TestPrimitive.class);

        assertThat(result.publicField).isInstanceOf(String.class);
        assertThat(result.getHello()).isInstanceOf(String.class);
        assertThat(result.getPrimitive()).isInstanceOf(Integer.class);
        assertThat(result.getInteger()).isInstanceOf(Integer.class);
    }

    @Test
    public void randomCollections() {
        var sut = new Randomizer();

        final TestCollections result = sut.random(TestCollections.class);

        assertThat(result.getList()).isInstanceOf(List.class);
        assertThat(result.getList().size()).isGreaterThanOrEqualTo(MIN_SIZE).isLessThanOrEqualTo(MAX_SIZE);
        assertThat(result.getArrayList()).isInstanceOf(ArrayList.class);

        assertThat(result.getSet()).isInstanceOf(Set.class);
        assertThat(result.getNavigableSet()).isInstanceOf(NavigableSet.class);
        assertThat(result.getSortedSet()).isInstanceOf(SortedSet.class);
        assertThat(result.getHashSet()).isInstanceOf(HashSet.class);
        assertThat(result.getTreeSet()).isInstanceOf(TreeSet.class);

        assertThat(result.getDeque()).isInstanceOf(Deque.class);
        assertThat(result.getArrayDeque()).isInstanceOf(ArrayDeque.class);
        assertThat(result.getBlockingDeque()).isInstanceOf(BlockingDeque.class);
        assertThat(result.getLinkedBlockingDeque()).isInstanceOf(LinkedBlockingDeque.class);

        assertThat(result.getQueue()).isInstanceOf(Queue.class);
        assertThat(result.getLinkedBlockingQueue()).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(result.getTransferQueue()).isInstanceOf(TransferQueue.class);
        assertThat(result.getLinkedTransferQueue()).isInstanceOf(LinkedTransferQueue.class);
        assertThat(result.getBlockingQueue()).isInstanceOf(BlockingQueue.class);
        assertThat(result.getLinkedBlockingQueue()).isInstanceOf(LinkedBlockingQueue.class);
    }

    @Test
    public void randomInterface() {
        var sut = new Randomizer();

        final TestInterface result = sut.random(TestInterface.class);

        assertThat(result.getTestObject()).isInstanceOf(TestPrimitive.class);
        assertThat(result.toString()).isInstanceOf(String.class);
        assertThat(result.publicField).isEqualTo(1);
    }

}

