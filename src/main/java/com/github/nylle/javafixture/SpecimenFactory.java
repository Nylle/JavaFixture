package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.AbstractSpecimen;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PredefinedSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.SpecialSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Type;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(final SpecimenType<T> type) {

        if (context.isCached(type)) {
            return new PredefinedSpecimen<>(type, context);
        }

        if (type.isPrimitive() || type.isBoxed() || type.asClass() == String.class) {
            return new PrimitiveSpecimen<>(type, context);
        }

        if (type.isEnum()) {
            return new EnumSpecimen<>(type);
        }

        if (type.isCollection()) {
            return new CollectionSpecimen<>(type, context, this);
        }

        if (type.isMap()) {
            return new MapSpecimen<>(type, context, this);
        }

        if (type.isParameterized() && !type.isInterface()) {
            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isParameterized() && type.isInterface()) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return implementationOrProxy(type);
            }

            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isArray()) {
            return new ArraySpecimen<>(type, context, this);
        }

        if (type.isTimeType()) {
            return new TimeSpecimen<>(type, context);
        }

        if (type.isInterface()) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return implementationOrProxy(type);
            }

            return new InterfaceSpecimen<>(type, context, this);
        }

        if (type.isAbstract()) {
            return new AbstractSpecimen<>(type, context, this);
        }

        if (type.isSpecialType()) {
            return new SpecialSpecimen<>(type, context);
        }

        return new ObjectSpecimen<>(type, context, this);
    }

    private <T> ISpecimen<T> implementationOrProxy(final SpecimenType<T> interfaceType) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            var implementingClasses = scanResult.getClassesImplementing(interfaceType.asClass());
            if (implementingClasses.isEmpty()) {
                return new InterfaceSpecimen<>(interfaceType, context, this);
            }

            var implementingClass = implementingClasses.get(new Random().nextInt(implementingClasses.size()));
            if (implementingClass.getTypeSignature() == null || implementingClass.getTypeSignature().getTypeParameters().isEmpty()) {
                return new ObjectSpecimen<>(SpecimenType.fromClass(implementingClass.loadClass()), context, this);
            }

            if (!interfaceType.isParameterized()) {
                return new InterfaceSpecimen<>(interfaceType, context, this);
            }

            var typeParameters = IntStream.range(0, interfaceType.getGenericTypeArguments().length)
                    .boxed()
                    .collect(toMap(
                            i -> interfaceType.getTypeParameterName(i),
                            i -> SpecimenType.fromClass(interfaceType.getGenericTypeArgument(i))));

            var actualTypeArguments = implementingClass.getTypeSignature().getTypeParameters().stream()
                    .map(x -> typeParameters.get(x.getName()).asClass())
                    .toArray(size -> new Type[size]);

            return new GenericSpecimen<>(
                    SpecimenType.fromRawType(implementingClass.loadClass(), actualTypeArguments),
                    context,
                    this);
        } catch (Exception ex) {
            return new InterfaceSpecimen<>(interfaceType, context, this);
        }
    }
}

