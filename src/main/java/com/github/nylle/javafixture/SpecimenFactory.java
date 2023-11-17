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
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Type;
import java.util.Random;
import java.util.stream.Collectors;
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
            var implementingClasses = scanResult.getClassesImplementing(interfaceType.asClass()).stream()
                    .filter(x -> interfaceType.isParameterized() || isNotParametrized(x))
                    .collect(Collectors.toList());

            if (implementingClasses.isEmpty()) {
                return new InterfaceSpecimen<>(interfaceType, context, this);
            }

            var implementingClass = implementingClasses.get(new Random().nextInt(implementingClasses.size()));
            if (isNotParametrized(implementingClass)) {
                return new ObjectSpecimen<>(SpecimenType.fromClass(implementingClass.loadClass()), context, this);
            }

            return new GenericSpecimen<>(
                    SpecimenType.fromRawType(implementingClass.loadClass(), resolveTypeArguments(interfaceType, implementingClass)),
                    context,
                    this);
        } catch (Exception ex) {
            return new InterfaceSpecimen<>(interfaceType, context, this);
        }
    }

    private static boolean isNotParametrized(ClassInfo classInfo) {
        return classInfo.getTypeSignature() == null || classInfo.getTypeSignature().getTypeParameters().isEmpty();
    }

    private static <T> Type[] resolveTypeArguments(SpecimenType<T> genericType, ClassInfo implementingClass) {
        // throws NPE if implementing class has more type arguments than interface
        // this was not intended, but luckily causes a fallback to proxy, because we wouldn't be able to resolve the additional type anyway
        var typeParameters = IntStream.range(0, genericType.getGenericTypeArguments().length)
                .boxed()
                .collect(toMap(
                        i -> genericType.getTypeParameterName(i),
                        i -> SpecimenType.fromClass(genericType.getGenericTypeArgument(i))));

        return implementingClass.getTypeSignature().getTypeParameters().stream()
                .map(x -> typeParameters.get(x.getName()).asClass())
                .toArray(size -> new Type[size]);
    }
}

