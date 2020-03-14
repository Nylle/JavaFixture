package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.generic.FixtureType;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> new MapSpecimen<>(FixtureType.fromClass(List.class), context, specimenFactory))
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
        assertThatThrownBy(() -> new MapSpecimen<>(FixtureType.fromClass(Map.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(FixtureType.fromClass(Map.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void nonParameterizedMapIsEmpty() {
        var actual = new MapSpecimen<>(new FixtureType<Map>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    void createHashMapFromMapInterface() {
        var actual = new MapSpecimen<>(new FixtureType<Map<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMapFromConcurrentNavigableMapInterface() {
        var actual = new MapSpecimen<>(new FixtureType<ConcurrentNavigableMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMapFromConcurrentMapInterface() {
        var actual = new MapSpecimen<>(new FixtureType<ConcurrentMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromNavigableMapInterface() {
        var actual = new MapSpecimen<>(new FixtureType<NavigableMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMapFromSortedMapInterface() {
        var actual = new MapSpecimen<>(new FixtureType<SortedMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createHashMap() {
        var actual = new MapSpecimen<>(new FixtureType<HashMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentSkipListMap() {
        var actual = new MapSpecimen<>(new FixtureType<ConcurrentSkipListMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentSkipListMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createConcurrentHashMap() {
        var actual = new MapSpecimen<>(new FixtureType<ConcurrentHashMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(ConcurrentHashMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void createTreeMap() {
        var actual = new MapSpecimen<>(new FixtureType<TreeMap<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(actual).isInstanceOf(TreeMap.class);
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.entrySet().stream()
                .allMatch(x -> x.getKey().getClass().equals(String.class) && x.getValue().getClass().equals(Integer.class)))
                .isTrue();
    }

    @Test
    void resultIsCached() {

        var original = new MapSpecimen<>(new FixtureType<Map<String, Integer>>(){}, context, specimenFactory).create();
        var cached = new MapSpecimen<>(new FixtureType<Map<String, Integer>>(){}, context, specimenFactory).create();

        assertThat(original).isInstanceOf(Map.class);
        assertThat(original.size()).isEqualTo(2);
        assertThat(original).isSameAs(cached);
        assertThat(original.get(0)).isEqualTo(cached.get(0));
        assertThat(original.get(1)).isEqualTo(cached.get(1));
    }

    @Test
    void nestedMaps() {
        var sut = new MapSpecimen<>(new FixtureType<Map<String, Map<String, Integer>>>(){}, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isExactlyInstanceOf(HashMap.class);
        assertThat(actual.size()).isEqualTo(2);

        for(var entry : actual.entrySet()) {
            assertThat(((Map.Entry)entry).getKey()).isExactlyInstanceOf(String.class);
            assertThat(((Map.Entry)entry).getValue()).isExactlyInstanceOf(HashMap.class);
            assertThat(((Map)((Map.Entry)entry).getValue()).size()).isEqualTo(2);
        }
    }

    @Test
    void nonPrimitiveElementsAreSameInstance() {

        var sut = new MapSpecimen<>(new FixtureType<HashMap<String, TestObject>>(){}, context, specimenFactory);

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