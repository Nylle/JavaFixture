package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.Random;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class EnumSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Random random;
    private final Context context;

    public EnumSpecimen(final SpecimenType<T> type, final Context context) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isEnum()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.random = new Random();
        this.context = context;
    }

    @Override
    public T create(Annotation[] annotations) {
        return create(noContext(), annotations);
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        return context.preDefined(type, type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)]);
    }
}
