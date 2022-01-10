package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
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
        var sut = new CollectionSpecimen<>(new SpecimenType<Collection>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    void createArrayListFromCollectionInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<Collection<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayListFromListInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<List<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromNavigableSetInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<NavigableSet<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSetFromSortedSetInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<SortedSet<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSetFromSetInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<Set<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingDequeFromBlockingDequeInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<BlockingDeque<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDequeFromDequeInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<Deque<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueueFromTransferQueueInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<TransferQueue<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueueFromBlockingQueueInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<BlockingQueue<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedListFromQueueInterface() {
        var sut = new CollectionSpecimen<>(new SpecimenType<Queue<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayList() {
        var sut = new CollectionSpecimen<>(new SpecimenType<ArrayList<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createTreeSet() {
        var sut = new CollectionSpecimen<>(new SpecimenType<TreeSet<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createHashSet() {
        var sut = new CollectionSpecimen<>(new SpecimenType<HashSet<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(HashSet.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createEnumSet() {
        var sut = new CollectionSpecimen<>(new SpecimenType<EnumSet<TestEnum>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isNotEmpty();
        assertThat(actual.iterator().next()).isInstanceOf(TestEnum.class);
    }

    @Test
    void createLinkedBlockingDeque() {
        var sut = new CollectionSpecimen<>(new SpecimenType<LinkedBlockingDeque<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createArrayDeque() {
        var sut = new CollectionSpecimen<>(new SpecimenType<ArrayDeque<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(ArrayDeque.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedTransferQueue() {
        var sut = new CollectionSpecimen<>(new SpecimenType<LinkedTransferQueue<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedBlockingQueue() {
        var sut = new CollectionSpecimen<>(new SpecimenType<LinkedBlockingQueue<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createLinkedList() {
        var sut = new CollectionSpecimen<>(new SpecimenType<LinkedList<String>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(LinkedList.class);
        assertThat(actual.stream().allMatch(x -> x.getClass().equals(String.class))).isTrue();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void resultIsCached() {

        var original = new CollectionSpecimen<>(new SpecimenType<List<String>>(){}, context, specimenFactory).create(new Annotation[0]);
        var cached = new CollectionSpecimen<>(new SpecimenType<List<String>>(){}, context, specimenFactory).create(new Annotation[0]);

        assertThat(original).isInstanceOf(List.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isSameAs(cached);
        assertThat(original.get(0)).isEqualTo(cached.get(0));
        assertThat(original.get(1)).isEqualTo(cached.get(1));
    }

    @Test
    void nestedLists() {
        var sut = new CollectionSpecimen<>(new SpecimenType<List<List<String>>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(2);

        assertThat(actual.get(0)).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.get(0).size()).isEqualTo(2);
        assertThat(actual.get(1)).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.get(1).size()).isEqualTo(2);
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        var sut = new CollectionSpecimen<>(new SpecimenType<List<TestObject>>(){}, context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isExactlyInstanceOf(ArrayList.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(1)).isExactlyInstanceOf(TestObject.class);
        assertThat(actual.get(0)).isSameAs(actual.get(1));

    }
}
