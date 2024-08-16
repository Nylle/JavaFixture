package com.github.nylle.javafixture;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ClassPathScanner {

    public <T> List<SpecimenType<? extends T>> findAllClassesFor(SpecimenType<T> type) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            return filter(scanResult, type).stream().map(x -> specimenTypeOf(x, type)).collect(Collectors.toList());
        } catch (Exception ex) {
            return List.of();
        }
    }

    private <T> List<ClassInfo> filter(ScanResult scanResult, SpecimenType<T> type) {
        if (type.isInterface()) {
            return scanResult.getClassesImplementing(type.asClass()).stream()
                    .filter(x -> isNotParametrized(x) || type.isParameterized())
                    .filter(x -> isNotParametrized(x) || typeParametersMatch(x, type))
                    .collect(Collectors.toList());
        }

        if (type.isAbstract()) {
            return scanResult.getSubclasses(type.asClass()).stream()
                    .filter(x -> !x.isAbstract())
                    .filter(x -> isNotParametrized(x) || type.isParameterized())
                    .filter(x -> isNotParametrized(x) || typeParametersMatch(x, type))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    private static <T, R extends T> SpecimenType<R> specimenTypeOf(ClassInfo implementingClass, SpecimenType<T> type) {
        if (isNotParametrized(implementingClass)) {
            return SpecimenType.fromClass(implementingClass.loadClass());
        }

        return SpecimenType.fromRawType(implementingClass.loadClass(), resolveTypeArguments(type, implementingClass));
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
