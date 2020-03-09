package com.github.nylle.javafixture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomizationContext {
    private final List<String> ignoredFields;
    private final Map<String, Object> customFields;


    private CustomizationContext() {
        ignoredFields = Collections.emptyList();
        customFields = new HashMap<>();
    }

    public CustomizationContext(final List<String> ignoredFields, final Map<String, Object> customFields) {
        this.ignoredFields = ignoredFields;
        this.customFields = customFields;
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
}
