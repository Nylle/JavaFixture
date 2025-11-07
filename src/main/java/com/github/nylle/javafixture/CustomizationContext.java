package com.github.nylle.javafixture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomizationContext {
    private final List<String> ignoredFields;
    private final Map<String, Object> customFields;
    private final boolean useRandomConstructor;

    private CustomizationContext() {
        ignoredFields = List.of();
        customFields = new HashMap<>();
        useRandomConstructor = false;
    }

    public CustomizationContext(final List<String> ignoredFields, final Map<String, Object> customFields, boolean useRandomConstructor) {
        this.ignoredFields = Collections.unmodifiableList(ignoredFields);
        this.customFields = Collections.unmodifiableMap(customFields);
        this.useRandomConstructor = useRandomConstructor;
    }

    public static CustomizationContext noContext() {
        return new CustomizationContext();
    }

    public List<String> getIgnoredFields() {
        return ignoredFields;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public boolean useRandomConstructor() {
        return useRandomConstructor;
    }

    public Stream<String> findAllCustomizedFieldNames() {
        return Stream.concat(customFields.keySet().stream(), ignoredFields.stream());
    }

    public CustomizationContext newForField(String name) {
        return new CustomizationContext(
                ignoredFields.stream()
                        .filter(x -> x.startsWith(name + "."))
                        .map(x -> x.substring(name.length() + 1))
                        .collect(Collectors.toList()),
                customFields.entrySet().stream()
                        .filter(x -> x.getKey().startsWith(name + "."))
                        .map(x -> Map.entry(x.getKey().substring(name.length() + 1), x.getValue()))
                        .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue())),
                useRandomConstructor);
    }
}
