package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
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
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyCollectionTypes() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(SpecimenType.fromClass(List.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new CollectionSpecimen<>(SpecimenType.fromClass(List.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void nonParameterizedCollectionIsEmpty() {
        CollectionSpecimen<Collection, Object> sut = new CollectionSpecimen<Collection, Object>(new SpecimenType<Collection>(){}, context, specimenFactory);

        Collection actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    void createArrayListFromCollectionInterface() {
        CollectionSpecimen<Collection<String>, Object> sut = new CollectionSpecimen<Collection<String>, Object>(new SpecimenType<Collection<String>>(){}, context, specimenFactory);

        Collection<String> actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayListFromListInterface() {
        CollectionSpecimen<List<String>, Object> sut = new CollectionSpecimen<List<String>, Object>(new SpecimenType<List<String>>(){}, context, specimenFactory);

        List<String> actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromNavigableSetInterface() {
        CollectionSpecimen<NavigableSet<String>, Object> sut = new CollectionSpecimen<NavigableSet<String>, Object>(new SpecimenType<NavigableSet<String>>(){}, context, specimenFactory);

        NavigableSet<String> actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromSortedSetInterface() {
        CollectionSpecimen<SortedSet<String>, Object> sut = new CollectionSpecimen<SortedSet<String>, Object>(new SpecimenType<SortedSet<String>>(){}, context, specimenFactory);

        SortedSet<String> actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSetFromSetInterface() {
        CollectionSpecimen<Set<String>, Object> sut = new CollectionSpecimen<Set<String>, Object>(new SpecimenType<Set<String>>(){}, context, specimenFactory);

        Set<String> actual = sut.create();

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingDequeFromBlockingDequeInterface() {
        CollectionSpecimen<BlockingDeque<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<BlockingDeque<String>>(){}, context, specimenFactory);

        BlockingDeque<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDequeFromDequeInterface() {
        CollectionSpecimen<Deque<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<Deque<String>>(){}, context, specimenFactory);

        Deque<String> actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueueFromTransferQueueInterface() {
        CollectionSpecimen<TransferQueue<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<TransferQueue<String>>(){}, context, specimenFactory);

        TransferQueue<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueueFromBlockingQueueInterface() {
        CollectionSpecimen<BlockingQueue<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<BlockingQueue<String>>(){}, context, specimenFactory);

        BlockingQueue<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedListFromQueueInterface() {
        CollectionSpecimen<Queue<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<Queue<String>>(){}, context, specimenFactory);

        Queue<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayList() {
        CollectionSpecimen<ArrayList<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<ArrayList<String>>(){}, context, specimenFactory);

        ArrayList<String> actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSet() {
        CollectionSpecimen<TreeSet<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<TreeSet<String>>(){}, context, specimenFactory);

        TreeSet<String> actual = sut.create();

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSet() {
        CollectionSpecimen<HashSet<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<HashSet<String>>(){}, context, specimenFactory);

        HashSet<String> actual = sut.create();

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createEnumSet() {
        CollectionSpecimen<EnumSet<TestEnum>, Object> sut = new CollectionSpecimen<>(new SpecimenType<EnumSet<TestEnum>>(){}, context, specimenFactory);

        EnumSet<TestEnum> actual = sut.create();

        assertThat(actual).isNotEmpty();
        assertThat(actual.iterator().next()).isInstanceOf(TestEnum.class);
    }

    @Test
    void createLinkedBlockingDeque() {
        CollectionSpecimen<LinkedBlockingDeque<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<LinkedBlockingDeque<String>>(){}, context, specimenFactory);

        LinkedBlockingDeque<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDeque() {
        CollectionSpecimen<ArrayDeque<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<ArrayDeque<String>>(){}, context, specimenFactory);

        ArrayDeque<String> actual = sut.create();

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueue() {
        CollectionSpecimen<LinkedTransferQueue<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<LinkedTransferQueue<String>>(){}, context, specimenFactory);

        LinkedTransferQueue<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueue() {
        CollectionSpecimen<LinkedBlockingQueue<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<LinkedBlockingQueue<String>>(){}, context, specimenFactory);

        LinkedBlockingQueue<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedList() {
        CollectionSpecimen<LinkedList<String>, Object> sut = new CollectionSpecimen<>(new SpecimenType<LinkedList<String>>(){}, context, specimenFactory);

        LinkedList<String> actual = sut.create();

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void resultIsCached() {

        List<String> original = new CollectionSpecimen<>(new SpecimenType<List<String>>(){}, context, specimenFactory).create();
        List<String> cached = new CollectionSpecimen<>(new SpecimenType<List<String>>(){}, context, specimenFactory).create();

        assertThat(original).isInstanceOf(List.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isSameAs(cached);
        assertThat(original.get(0)).isEqualTo(cached.get(0));
        assertThat(original.get(1)).isEqualTo(cached.get(1));
    }

    @Test
    void nestedLists() {
        CollectionSpecimen<List<List<String>>, Object> sut = new CollectionSpecimen<List<List<String>>, Object>(new SpecimenType<List<List<String>>>(){}, context, specimenFactory);

        List<List<String>> actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(2);

        assertThat(actual.get(0)).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.get(0).size()).isEqualTo(2);
        assertThat(actual.get(1)).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.get(1).size()).isEqualTo(2);
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        CollectionSpecimen<List<TestObject>, Object> sut = new CollectionSpecimen<List<TestObject>, Object>(new SpecimenType<List<TestObject>>(){}, context, specimenFactory);

        List<TestObject> actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(1)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(0)).isSameAs(actual.get(1));

    }
}
