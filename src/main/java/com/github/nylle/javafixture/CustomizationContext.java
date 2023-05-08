package com.github.nylle.javafixture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
