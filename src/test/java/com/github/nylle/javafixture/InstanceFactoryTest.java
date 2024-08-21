package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithConcreteMethod;
import com.github.nylle.javafixture.testobjects.factorymethod.ConstructorExceptionAndFactoryMethod;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithGenericArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithItselfAsArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithOnlyItselfAsArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithoutArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.GenericClassWithFactoryMethodWithoutArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.TestObjectWithNonPublicFactoryMethods;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithDefaultMethod;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithConstructedField;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithGenericConstructor;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithPrivateConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static com.github.nylle.javafixture.SpecimenType.fromClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class InstanceFactoryTest {

    @Nested
    @DisplayName("when using constructor")
    class UsingConstructor {

        @Test
        @DisplayName("instance is created from random constructor")
        void canCreateInstanceFromConstructor() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class), new CustomizationContext(List.of(), Map.of(), false));

            assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
            assertThat(result.getValue()).isInstanceOf(String.class);
            assertThat(result.getInteger()).isInstanceOf(Optional.class);
        }

        @Test
        @DisplayName("fields not set by constructor are null")
        void fieldsNotSetByConstructorAreNull() {

            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class), new CustomizationContext(List.of(), Map.of(), false));

            assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
            assertThat(result.getPrivateField()).isNull();
        }

        @Test
        @DisplayName("using constructor is used for all instances")
        void usingConstructorIsRecursive() {

            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            TestObjectWithConstructedField result = sut.construct(fromClass(TestObjectWithConstructedField.class), new CustomizationContext(List.of(), Map.of(), true));

            assertThat(result).isInstanceOf(TestObjectWithConstructedField.class);
            assertThat(result.getTestObjectWithGenericConstructor().getPrivateField()).isNull();
            assertThat(result.getNotSetByConstructor()).isNull();
        }

        @Test
        @DisplayName("construction will fail if no public constructor is available")
        void canOnlyUsePublicConstructor() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.construct(fromClass(TestObjectWithPrivateConstructor.class), new CustomizationContext(List.of(), Map.of(), false)))
                    .withMessageContaining("Cannot manufacture class")
                    .withNoCause();
        }

        @Test
        @DisplayName("will fallback to factory method when no public constructor exists")
        void useFactoryMethodWhenNoConstructorExists() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            FactoryMethodWithoutArgument result = sut.construct(fromClass(FactoryMethodWithoutArgument.class), new CustomizationContext(List.of(), Map.of(), false));

            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        @DisplayName("will fallback to factor method when constructor fails")
        void fallbackToFactoryMethodWhenConstructorThrowsException() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var result = sut.construct(new SpecimenType<ConstructorExceptionAndFactoryMethod>() {}, new CustomizationContext(List.of(), Map.of(), false));

            assertThat(result.getValue()).isNotNull();
        }

        @Test
        @DisplayName("arguments can be customized")
        void argumentsCanBeCustomized() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            // use arg0, because .class files do not store formal parameter names by default
            var customizationContext = new CustomizationContext(List.of(), Map.of("arg0", "customized"), true);
            TestObject result = sut.construct(fromClass(TestObject.class), customizationContext);

            assertThat(result.getValue()).isEqualTo("customized");
        }

        @Test
        @DisplayName("constructor arguments are cached")
        void constructorArgumentsAreCached() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var customizationContext = new CustomizationContext(List.of(), Map.of(), true);
            TestObject first = sut.construct(fromClass(TestObject.class), customizationContext);
            TestObject second = sut.construct(fromClass(TestObject.class), customizationContext);

            assertThat(first.getIntegers()).usingRecursiveComparison().isEqualTo(second.getIntegers());
            assertThat(first.getStrings()).usingRecursiveComparison().isEqualTo(second.getStrings());
            assertThat(first.getValue()).as("primitives are never cached").isNotEqualTo(second.getValue());
        }

        @Test
        @DisplayName("customized arguments are only used for the top level object (no nested objects)")
        void constructorArgumentsAreUsedOnce() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var customizationContext = new CustomizationContext(List.of(), Map.of("arg0", 2), true);
            TestObjectWithConstructedField result = sut.construct(fromClass(TestObjectWithConstructedField.class), customizationContext);

            assertThat(result.getSetByConstructor()).isEqualTo(2);
        }

        @Test
        @DisplayName("customized arguments are used for exclusion, too")
        void ignoredConstructorArgsAreRespected() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var customizationContext = new CustomizationContext(List.of("arg0"), Map.of(), true);
            TestObjectWithConstructedField result = sut.construct(fromClass(TestObjectWithConstructedField.class), customizationContext);

            assertThat(result.getSetByConstructor()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("when manufacturing using factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("factory method from abstract class is used when present")
        void canCreateInstanceFromAbstractClassUsingFactoryMethod() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.manufacture(new SpecimenType<Charset>() {}, noContext());

            assertThat(actual).isInstanceOf(Charset.class);
        }

        @Test
        @DisplayName("only public methods will be used")
        void canOnlyUsePublicFactoryMethods() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.manufacture(fromClass(TestObjectWithNonPublicFactoryMethods.class), noContext()))
                    .withMessageContaining("Cannot manufacture class")
                    .withNoCause();
        }

        @Test
        @DisplayName("method arguments are used")
        void factoryMethodWithArgument() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            FactoryMethodWithArgument result = sut.manufacture(fromClass(FactoryMethodWithArgument.class), noContext());

            assertThat(result.getValue()).isNotNull();
        }

        @Test
        @DisplayName("methods with class itself as argument should be filtered out")
        void shouldFilter() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            FactoryMethodWithItselfAsArgument result = sut.manufacture(fromClass(FactoryMethodWithItselfAsArgument.class), noContext());

            assertThat(result.getValue()).isNull();
        }

        @Test
        @DisplayName("if only methods with class itself are available, we should fail")
        void shouldFailWithoutValidFactoryMethod() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.manufacture(fromClass(FactoryMethodWithOnlyItselfAsArgument.class), noContext()));

        }

        @Test
        @DisplayName("optional can be created (and may be empty)")
        void createOptional() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var result = sut.manufacture(new SpecimenType<Optional<String>>() {}, noContext());

            assertThat(result).isInstanceOf(Optional.class);
            assertThat(result.orElse("optional may be empty")).isInstanceOf(String.class);
        }

        @Test
        @DisplayName("method without arguments is used")
        void factoryMethodWithoutArgument() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            FactoryMethodWithoutArgument result = sut.manufacture(fromClass(FactoryMethodWithoutArgument.class), noContext());

            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        @DisplayName("method with generic arguments is used")
        void factoryMethodWithGenericArgument() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var result = sut.manufacture(new SpecimenType<FactoryMethodWithGenericArgument<Integer>>() {}, noContext());

            assertThat(result.getValue()).isNotNull();
        }

        @Test
        @DisplayName("method without arguments is used")
        void genericNoArgumentFactoryMethod() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var result = sut.manufacture(new SpecimenType<GenericClassWithFactoryMethodWithoutArgument<Integer>>() {}, noContext());

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(42);
        }

    }

    @Nested
    class CreateCollectionReturns {

        @Test
        void arrayListFromCollectionInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Collection<String>>() {});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayListFromListInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<List<String>>() {});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSetFromNavigableSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<NavigableSet<String>>() {});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSetFromSortedSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<SortedSet<String>>() {});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void hashSetFromSetInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Set<String>>() {});

            assertThat(actual).isInstanceOf(HashSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingDequeFromBlockingDequeInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<BlockingDeque<String>>() {});

            assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayDequeFromDequeInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Deque<String>>() {});

            assertThat(actual).isInstanceOf(ArrayDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedTransferQueueFromTransferQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<TransferQueue<String>>() {});

            assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingQueueFromBlockingQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<BlockingQueue<String>>() {});

            assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedListFromQueueInterface() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<Queue<String>>() {});

            assertThat(actual).isInstanceOf(LinkedList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayList() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<ArrayList<String>>() {});

            assertThat(actual).isInstanceOf(ArrayList.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void treeSet() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<TreeSet<String>>() {});

            assertThat(actual).isInstanceOf(TreeSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void hashSet() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<HashSet<String>>() {});

            assertThat(actual).isInstanceOf(HashSet.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingDeque() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedBlockingDeque<String>>() {});

            assertThat(actual).isInstanceOf(LinkedBlockingDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void arrayDeque() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<ArrayDeque<String>>() {});

            assertThat(actual).isInstanceOf(ArrayDeque.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedTransferQueue() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedTransferQueue<String>>() {});

            assertThat(actual).isInstanceOf(LinkedTransferQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedBlockingQueue() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedBlockingQueue<String>>() {});

            assertThat(actual).isInstanceOf(LinkedBlockingQueue.class);
            assertThat(actual).isEmpty();
        }

        @Test
        void linkedList() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = sut.createCollection(new SpecimenType<LinkedList<String>>() {});

            assertThat(actual).isInstanceOf(LinkedList.class);
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("when creating proxy")
    class ProxyTest {

        @DisplayName("if we create from interface, default methods are called and not fixtured")
        @Test
        void callsDefaultMethods() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = (InterfaceWithDefaultMethod) sut.proxy(new SpecimenType<InterfaceWithDefaultMethod>() {}, new HashMap<String, ISpecimen<?>>());

            assertThat(actual.getTestObject()).isNotNull();
            assertThat(actual.getDefaultInt()).isEqualTo(42);
        }

        @DisplayName("if we create from an abstract class, public methods are called and not fixtured")
        @Test
        void fromAbstractClass() {
            var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

            var actual = (AbstractClassWithConcreteMethod) sut.proxy(new SpecimenType<AbstractClassWithConcreteMethod>() {}, new HashMap<String, ISpecimen<?>>());

            assertThat(actual.getTestObject()).isNotNull();
            assertThat(actual.getDefaultInt()).isEqualTo(42);
        }
    }
}
