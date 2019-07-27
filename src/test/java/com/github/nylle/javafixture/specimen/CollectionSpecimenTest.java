package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.DefaultSpecimenFactory;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CollectionSpecimenTest {

    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new DefaultSpecimenFactory(context);
    }
    
    @Test
    void onlyCollectionTypes() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(Map.class, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(null, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(List.class, Object.class, null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(List.class, Object.class, context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createArrayListFromListInterface() {
        var sut = new CollectionSpecimen<>(List.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromNavigableSetInterface() {
        var sut = new CollectionSpecimen<>(NavigableSet.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromSortedSetInterface() {
        var sut = new CollectionSpecimen<>(SortedSet.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSetFromSetInterface() {
        var sut = new CollectionSpecimen<>(Set.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingDequeFromBlockingDequeInterface() {
        var sut = new CollectionSpecimen<>(BlockingDeque.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDequeFromDequeInterface() {
        var sut = new CollectionSpecimen<>(Deque.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueueFromTransferQueueInterface() {
        var sut = new CollectionSpecimen<>(TransferQueue.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueueFromBlockingQueueInterface() {
        var sut = new CollectionSpecimen<>(BlockingQueue.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedListFromQueueInterface() {
        var sut = new CollectionSpecimen<>(Queue.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayList() {
        var sut = new CollectionSpecimen<>(ArrayList.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSet() {
        var sut = new CollectionSpecimen<>(TreeSet.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSet() {
        var sut = new CollectionSpecimen<>(HashSet.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingDeque() {
        var sut = new CollectionSpecimen<>(LinkedBlockingDeque.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDeque() {
        var sut = new CollectionSpecimen<>(ArrayDeque.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueue() {
        var sut = new CollectionSpecimen<>(LinkedTransferQueue.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueue() {
        var sut = new CollectionSpecimen<>(LinkedBlockingQueue.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedList() {
        var sut = new CollectionSpecimen<>(LinkedList.class, String.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void resultIsCached() {

        var original = new CollectionSpecimen<>(List.class, String.class, context, specimenFactory).create();
        var cached = new CollectionSpecimen<>(List.class, String.class, context, specimenFactory).create();

        assertThat(original).isInstanceOf(List.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isSameAs(cached);
        assertThat(original.get(0)).isEqualTo(cached.get(0));
        assertThat(original.get(1)).isEqualTo(cached.get(1));
    }

    @Test
    void nestedListsLoseGenericTypeAndAreEmpty() {
        var sut = new CollectionSpecimen<>(List.class, List.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(((ArrayList<ArrayList<Object>>)actual).size()).isEqualTo(2);

        assertThat(actual.get(0)).isExactlyInstanceOf(ArrayList.class);
        assertThat(((List)actual.get(0)).size()).isEqualTo(0);
        assertThat(actual.get(1)).isExactlyInstanceOf(ArrayList.class);
        assertThat(((List)actual.get(1)).size()).isEqualTo(0);
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        var sut = new CollectionSpecimen<>(List.class, TestObject.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(1)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(0)).isSameAs(actual.get(1));

    }
}