package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithGenericConstructor;
import com.github.nylle.javafixture.testobjects.TestObjectWithNonPublicFactoryMethods;
import com.github.nylle.javafixture.testobjects.TestObjectWithPrivateConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
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

import static com.github.nylle.javafixture.SpecimenType.fromClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class InstanceFactoryTest {

    @Test
    void canCreateInstanceFromConstructor() {
        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getValue()).isInstanceOf(String.class);
        assertThat(result.getInteger()).isInstanceOf(Optional.class);
        assertThat(result.getInteger()).isPresent();
        assertThat(result.getInteger().get()).isInstanceOf(Integer.class);
    }

    @Test
    void fieldsNotSetByConstructorAreNull() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getPrivateField()).isNull();
    }

    @Test
    void canOnlyUsePublicConstructor() {
        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        assertThatExceptionOfType(SpecimenException.class)
                .isThrownBy(() -> sut.construct(fromClass(TestObjectWithPrivateConstructor.class)))
                .withMessageContaining("no public constructor found")
                .withNoCause();
    }

    @Test
    void canCreateInstanceFromAbstractClassUsingFactoryMethod() {
        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        var actual = sut.manufacture(new SpecimenType<Charset>() {});

        assertThat(actual).isInstanceOf(Charset.class);
    }

    @Test
    void canOnlyUsePublicFactoryMethods() {
        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        assertThatExceptionOfType(SpecimenException.class)
                .isThrownBy(() -> sut.manufacture(fromClass(TestObjectWithNonPublicFactoryMethods.class)))
                .withMessageContaining("Cannot manufacture class")
                .withNoCause();
    }

    @Nested
    class CreateCollectionReturns {

        @Test
        void arrayListFromCollectionInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Collection<String>>(){});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayListFromListInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<List<String>>(){});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSetFromNavigableSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<NavigableSet<String>>(){});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSetFromSortedSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<SortedSet<String>>(){});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void hashSetFromSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Set<String>>(){});

            assertThat(actual).isInstanceOf(HashSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingDequeFromBlockingDequeInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<BlockingDeque<String>>(){});

            assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayDequeFromDequeInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Deque<String>>(){});

            assertThat(actual).isInstanceOf(ArrayDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedTransferQueueFromTransferQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<TransferQueue<String>>(){});

            assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingQueueFromBlockingQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<BlockingQueue<String>>(){});

            assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedListFromQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Queue<String>>(){});

            assertThat(actual).isInstanceOf(LinkedList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayList() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<ArrayList<String>>(){});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSet() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<TreeSet<String>>(){});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void hashSet() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<HashSet<String>>(){});

            assertThat(actual).isInstanceOf(HashSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingDeque() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedBlockingDeque<String>>(){});

            assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayDeque() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<ArrayDeque<String>>(){});

            assertThat(actual).isInstanceOf(ArrayDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedTransferQueue() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedTransferQueue<String>>(){});

            assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingQueue() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedBlockingQueue<String>>(){});

            assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedList() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedList<String>>(){});

            assertThat(actual).isInstanceOf(LinkedList.class);
            assertThat(actual).isEmpty();
        }
    }
}
