package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;

public class PredefinedSpecimen<T> implements ISpecimen<T> {

    private final Context context;
    private final SpecimenType<T> type;

    public PredefinedSpecimen(SpecimenType<T> type, Context context) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.context = context;
        this.type = type;
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        return context.cached(type);
    }
}
