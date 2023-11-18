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

        if (type.isParameterized() && !type.isInterface() && !type.isAbstract()) {
            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isParameterized() && type.isInterface()) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return implementationOrProxy(type);
            }

            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isParameterized() && type.isAbstract()) {
            if (context.getConfiguration().experimentalInterfaces() && type.isAbstract()) {
                return subClassOrProxy(type);
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
            if (context.getConfiguration().experimentalInterfaces()) {
                return subClassOrProxy(type);
            }
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
                    .filter(x -> isNotParametrized(x) || interfaceType.isParameterized())
                    .filter(x -> isNotParametrized(x) || typeParametersMatch(x, interfaceType))
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

    private <T> ISpecimen<T> subClassOrProxy(final SpecimenType<T> abstractType) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            var subClasses = scanResult.getSubclasses(abstractType.asClass()).stream()
                    .filter(x -> !x.isAbstract())
                    .filter(x -> isNotParametrized(x) || abstractType.isParameterized())
                    .filter(x -> isNotParametrized(x) || typeParametersMatch(x, abstractType))
                    .collect(Collectors.toList());

            if (subClasses.isEmpty()) {
                return new AbstractSpecimen<>(abstractType, context, this);
            }

            var implementingClass = subClasses.get(new Random().nextInt(subClasses.size()));
            if (isNotParametrized(implementingClass)) {
                return new ObjectSpecimen<>(SpecimenType.fromClass(implementingClass.loadClass()), context, this);
            }

            return new GenericSpecimen<>(
                    SpecimenType.fromRawType(implementingClass.loadClass(), resolveTypeArguments(abstractType, implementingClass)),
                    context,
                    this);
        } catch (Exception ex) {
            return new AbstractSpecimen<>(abstractType, context, this);
        }
    }

    private static boolean isNotParametrized(ClassInfo classInfo) {
        return classInfo.getTypeSignature() == null || classInfo.getTypeSignature().getTypeParameters().isEmpty();
    }

    private static <T> boolean typeParametersMatch(ClassInfo implementingClass, SpecimenType<T> genericType) {
        return resolveTypeArguments(genericType, implementingClass).length >= implementingClass.getTypeSignature().getTypeParameters().size();
    }

    private static <T> Type[] resolveTypeArguments(SpecimenType<T> genericType, ClassInfo implementingClass) {
        var typeParameters = genericType.getTypeParameterNamesAndTypes(x -> x);

        return implementingClass.getTypeSignature().getTypeParameters().stream()
                .map(x -> typeParameters.getOrDefault(x.getName(), null))
                .filter(x -> x != null)
                .map(x -> x.asClass())
                .toArray(size -> new Type[size]);
    }
}

