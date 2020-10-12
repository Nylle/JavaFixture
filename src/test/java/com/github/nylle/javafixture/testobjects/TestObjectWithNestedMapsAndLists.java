package com.github.nylle.javafixture.testobjects;

import java.util.List;
import java.util.Map;

public class TestObjectWithNestedMapsAndLists {

    private List<List<String>> nestedList;

    private Map<String, Map<String, String>> mapWithMap;

    private Map<String, List<String>> mapWithList;

    private List<Map<String, List<String>>> deeplyNestedList;

    public List<Map<String, List<String>>> getDeeplyNestedList() {
        return deeplyNestedList;
    }

    public Map<String, Map<String, String>> getMapWithMap() {
        return mapWithMap;
    }

    public List<List<String>> getNestedList() {
        return nestedList;
    }

    public Map<String, List<String>> getMapWithList() {
        return mapWithList;
    }
}
