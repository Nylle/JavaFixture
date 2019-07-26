package com.github.nylle.javafixture;

public class Randomizer {

    final SpecimenFactory specimenFactory = new SpecimenFactory(new Context(new Configuration()));
    final ProxyFactory proxyFactory = new ProxyFactory();

    public <T> T random(final Class<T> type) {

        if (type.isPrimitive() || type.isEnum() || Reflector.isBoxedOrString(type)) {
            return specimenFactory.build(type).create();
        }

        if (Reflector.isMap(type)) {
            //TODO: non-generic empty map or exception?
            throw new SpecimenException("Unsupported type: " + type);
        }

        if (Reflector.isCollection(type)) {
            return specimenFactory.build(type).create();
        }

        if (type.isInterface()) {
            return proxyFactory.create(type, this);
        }

        //TODO: abstract classes: Modifier.isAbstract(type.getModifiers());

        return null;
    }
}

