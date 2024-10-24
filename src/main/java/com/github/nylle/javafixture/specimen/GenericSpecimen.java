package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

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

        if (!supportsType(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);
        this.specimens = type.getTypeParameterNamesAndTypes(x -> specimenFactory.build(x));
    }

    public static <T> boolean supportsType(SpecimenType<T> type) {
        return type.isParameterized() && !type.isCollection() && !type.isMap();
    }

    public static IMeta meta() {
        return new IMeta() {
            @Override
            public <T> boolean supports(SpecimenType<T> type) {
                return supportsType(type);
            }

            @Override
            public <T> ISpecimen<T> create(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {
                return new GenericSpecimen<>(type, context, specimenFactory);
            }
        };
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        if (type.asClass().equals(Class.class)) {
            return (T) specimens.entrySet().stream().findFirst().get().getValue().create(customizationContext, new Annotation[0]).getClass();
        }

        if (context.isCached(type)) {
            return context.cached(type);
        }

        if (type.isInterface()) {
            //TODO: shouldn't this be covered by InterfaceSpecimen?
            return (T) instanceFactory.proxy(type, specimens);
        }

        if (customizationContext.useRandomConstructor()) {
            return instanceFactory.construct(type, customizationContext);
        }

        return populate(customizationContext);
    }

    private T populate(CustomizationContext customizationContext) {
        var result = context.cached(type, instanceFactory.instantiate(type));
        var reflector = new Reflector<>(result).validateCustomization(customizationContext, type);
        try {
            reflector.getDeclaredFields()
                    .filter(field -> !customizationContext.getIgnoredFields().contains(field.getName()))
                    .forEach(field -> reflector.setField(field,
                            customizationContext.getCustomFields().getOrDefault(
                                    field.getName(),
                                    specimens.getOrDefault(
                                            field.getGenericType().getTypeName(),
                                            specimenFactory.build(SpecimenType.fromClass(field.getType()))).create(new CustomizationContext(List.of(), Map.of(), false), new Annotation[0]))));
        } catch (SpecimenException ex) {
            context.remove(type);
            return instanceFactory.construct(type, customizationContext);
        }
        return context.remove(type);
    }
}

