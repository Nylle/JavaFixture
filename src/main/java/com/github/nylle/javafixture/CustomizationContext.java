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
        ignoredFields = Collections.emptyList();
        customFields = new HashMap<>();
        useRandomConstructor = false;
    }

    public CustomizationContext(final boolean useRandomConstructor) {
        ignoredFields = Collections.emptyList();
        customFields = new HashMap<>();
        this.useRandomConstructor = useRandomConstructor;
    }

    public CustomizationContext(final List<String> ignoredFields, final Map<String, Object> customFields) {
        this.ignoredFields = ignoredFields;
        this.customFields = customFields;
        this.useRandomConstructor = false;
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
