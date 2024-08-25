package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapSpecimenTest {
    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Nested
    class WhenConstructing {

        @Test
        void onlyMapTypes() {
            assertThatThrownBy(() -> new MapSpecimen<>(SpecimenType.fromClass(List.class), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + List.class.getName());
        }

        @Test
        void typeIsRequired() {
            assertThatThrownBy(() -> new MapSpecimen<>(null, context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        void contextIsRequired() {
            assertThatThrownBy(() -> new MapSpecimen<>(SpecimenType.fromClass(Map.class), null, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }

        @Test
        void specimenFactoryIsRequired() {
            assertThatThrownBy(() -> new MapSpecimen<>(SpecimenType.fromClass(Map.class), context, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("specimenFactory: null");
        }
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = Map.class, bool2 = true)
    void supportsType(Class<?> type, boolean expected) {
        assertThat(MapSpecimen.supportsType(SpecimenType.fromClass(type))).isEqualTo(expected);
    }

    @Test
    void nonParameterizedMapIsEmpty() {
        var actual = new MapSpecimen<>(new SpecimenType<Map>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    void createHashMapFromMapInterface() {
        var actual = new MapSpecimen<>(new SpecimenType<Map<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMapFromConcurrentNavigableMapInterface() {
        var actual = new MapSpecimen<>(new SpecimenType<ConcurrentNavigableMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMapFromConcurrentMapInterface() {
        var actual = new MapSpecimen<>(new SpecimenType<ConcurrentMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromNavigableMapInterface() {
        var actual = new MapSpecimen<>(new SpecimenType<NavigableMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromSortedMapInterface() {
        var actual = new MapSpecimen<>(new SpecimenType<SortedMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createHashMap() {
        var actual = new MapSpecimen<>(new SpecimenType<HashMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMap() {
        var actual = new MapSpecimen<>(new SpecimenType<ConcurrentSkipListMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMap() {
        var actual = new MapSpecimen<>(new SpecimenType<ConcurrentHashMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMap() {
        var actual = new MapSpecimen<>(new SpecimenType<TreeMap<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createEnumMap() {
        var actual = new MapSpecimen<>(new SpecimenType<EnumMap<TestEnum, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(EnumMap.class);
        assertThat(actual).isNotEmpty();
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(TestEnum.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void resultIsNotCached() {

        var original = new MapSpecimen<>(new SpecimenType<Map<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);
        var second = new MapSpecimen<>(new SpecimenType<Map<String, Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(original).isInstanceOf(Map.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isNotEqualTo(second);
        assertThat(original.values()).doesNotContainAnyElementsOf(second.values());
    }

    @Test
    void nestedMaps() {
        var sut = new MapSpecimen<>(new SpecimenType<Map<String, Map<String, Integer>>>() {}, context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isExactlyInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);

        for (var entry : actual.entrySet()) {
            assertThat(((Map.Entry) entry).getKey()).isExactlyInstanceOf(String.class);
            assertThat(((Map.Entry) entry).getValue()).isExactlyInstanceOf(HashMap.class);
            assertThat(((Map) ((Map.Entry) entry).getValue()).size()).isEqualTo(2);
        }
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        var sut = new MapSpecimen<>(new SpecimenType<HashMap<String, TestObject>>() {}, context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isExactlyInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);

        var first = (Map.Entry) actual.entrySet().iterator().next();
        assertThat(first.getKey()).isExactlyInstanceOf(String.class);
        assertThat(first.getValue()).isExactlyInstanceOf(TestObject.class);

        var second = (Map.Entry) actual.entrySet().iterator().next();
        assertThat(second.getKey()).isExactlyInstanceOf(String.class);
        assertThat(second.getValue()).isExactlyInstanceOf(TestObject.class);

        assertThat(first.getValue()).isSameAs(second.getValue());
    }

    @Nested
    class SpecTest {

        @TestWithCases
        @TestCase(class1 = String.class, bool2 = false)
        @TestCase(class1 = Map.class, bool2 = true)
        void supports(Class<?> type, boolean expected) {
            assertThat(new MapSpecimen.Spec().supports(SpecimenType.fromClass(type))).isEqualTo(expected);
        }

        @Test
        void createReturnsNewSpecimen() {
            assertThat(new MapSpecimen.Spec().create(SpecimenType.fromClass(Map.class), context, specimenFactory))
                    .isInstanceOf(MapSpecimen.class);
        }

        @Test
        void createThrows() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new MapSpecimen.Spec().create(SpecimenType.fromClass(String.class), context, specimenFactory))
                    .withMessageContaining("type: java.lang.String");
        }
    }
}
