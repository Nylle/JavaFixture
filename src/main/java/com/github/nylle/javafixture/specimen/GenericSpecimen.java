package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.util.Map;
import java.util.stream.IntStream;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static java.util.stream.Collectors.toMap;

public class GenericSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;
    private final Map<String, ISpecimen<?>> specimens;

    public GenericSpecimen(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isParameterized()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (type.isCollection() || type.isMap()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);

        this.specimens = IntStream.range(0, type.getGenericTypeArguments().length)
                .boxed()
                .collect(toMap(
                        i -> type.getTypeParameterName(i),
                        i -> specimenFactory.build(SpecimenType.fromClass(type.getGenericTypeArgument(i)))));
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(CustomizationContext customizationContext) {
        if (type.asClass().equals(Class.class)) {
            return (T) specimens.entrySet().stream().findFirst().get().getValue().create().getClass();
        }

        if (context.isCached(type)) {
            return context.cached(type);
        }

        if (type.isInterface()) {
            return (T) context.cached(type, instanceFactory.proxy(type, specimens));
        }

        if (customizationContext.useRandomConstructor()) {
            return context.cached(type, instanceFactory.construct(type));
        }

        return new Reflector<>(context.cached(type, instanceFactory.instantiate(type)), specimenFactory)
                .validateCustomization(customizationContext, type)
                .populate(customizationContext, specimens);
    }
}

