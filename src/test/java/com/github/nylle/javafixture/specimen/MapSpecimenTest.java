package com.github.nylle.javafixture.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;

class MapSpecimenTest {
    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyMapTypes() {
        assertThatThrownBy(() -> new MapSpecimen<>(List.class, String.class, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + List.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(null, String.class, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void genericKeyTypeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, null, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericKeyType: null");
    }

    @Test
    void genericValueTypeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericValueType: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, Object.class, null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, Object.class, context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createHashMapFromMapInterface() {
        var actual = new MapSpecimen<>(Map.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((HashMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMapFromConcurrentNavigableMapInterface() {
        var actual = new MapSpecimen<>(ConcurrentNavigableMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((ConcurrentSkipListMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMapFromConcurrentMapInterface() {
        var actual = new MapSpecimen<>(ConcurrentMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((ConcurrentHashMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromNavigableMapInterface() {
        var actual = new MapSpecimen<>(NavigableMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((TreeMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromSortedMapInterface() {
        var actual = new MapSpecimen<>(SortedMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((TreeMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createHashMap() {
        var actual = new MapSpecimen<>(HashMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((HashMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMap() {
        var actual = new MapSpecimen<>(ConcurrentSkipListMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((ConcurrentSkipListMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMap() {
        var actual = new MapSpecimen<>(ConcurrentHashMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((ConcurrentHashMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMap() {
        var actual = new MapSpecimen<>(TreeMap.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(((TreeMap<String, Integer>)actual).entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void resultIsCached() {

        var original = new MapSpecimen<>(Map.class, String.class, Integer.class, context, specimenFactory).create();
        var cached = new MapSpecimen<>(Map.class, String.class, Integer.class, context, specimenFactory).create();

        assertThat(original).isInstanceOf(Map.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isSameAs(cached);
        assertThat(original.get(0)).isEqualTo(cached.get(0));
        assertThat(original.get(1)).isEqualTo(cached.get(1));
    }

    @Test
    void nestedMapsLoseGenericTypeAndAreEmpty() {
        var sut = new MapSpecimen<>(Map.class, String.class, Map.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);

        for(var entry : actual.entrySet()) {
            assertThat(((Map.Entry)entry).getKey()).isExactlyInstanceOf(String.class);
            assertThat(((Map.Entry)entry).getValue()).isExactlyInstanceOf(HashMap.class);
            assertThat(((Map)((Map.Entry)entry).getValue()).size()).isEqualTo(0);
        }
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        var sut = new MapSpecimen<>(HashMap.class, String.class, TestObject.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);

        var first = (Map.Entry)actual.entrySet().iterator().next();
        assertThat(first.getKey()).isExactlyInstanceOf(String.class);
        assertThat(first.getValue()).isExactlyInstanceOf(TestObject.class);

        var second = (Map.Entry)actual.entrySet().iterator().next();
        assertThat(second.getKey()).isExactlyInstanceOf(String.class);
        assertThat(second.getValue()).isExactlyInstanceOf(TestObject.class);

        assertThat(first.getValue()).isSameAs(second.getValue());
    }
}