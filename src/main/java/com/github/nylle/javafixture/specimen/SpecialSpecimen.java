package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenType;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class SpecialSpecimen<T> implements ISpecimen<T> {
    private final SpecimenType<T> type;
    private final Context context;


    public SpecialSpecimen(SpecimenType<T> type, Context context) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isSpecialType()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.context = context;
    }

    @Override
    public T create(Annotation[] annotations) {
        return create(CustomizationContext.noContext(), annotations);
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        if (type.asClass().equals(File.class)) {
            return (T) new File(UUID.randomUUID().toString());
        }
        try {
            return (T) new URI("https://localhost/" + UUID.randomUUID());
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
