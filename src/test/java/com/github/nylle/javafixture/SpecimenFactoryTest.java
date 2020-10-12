package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.example.IContract;

class SpecimenFactoryTest {

    @TestWithCases
    @TestCase(class1 = String.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = Integer.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = int.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = TestEnum.class, class2 = EnumSpecimen.class)
    @TestCase(class1 = Map.class, class2 = MapSpecimen.class)
    @TestCase(class1 = List.class, class2 = CollectionSpecimen.class)
    @TestCase(class1 = int[].class, class2 = ArraySpecimen.class)
    @TestCase(class1 = IContract.class, class2 = InterfaceSpecimen.class)
    @TestCase(class1 = Duration.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = Period.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = MonthDay.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZoneId.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZonedDateTime.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZoneOffset.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = Instant.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = java.util.Date.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = java.sql.Date.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = Object.class, class2 = ObjectSpecimen.class)
    void build(Class<?> value, Class<?> expected) {
        assertThat(new SpecimenFactory(new Context(new Configuration())).build(SpecimenType.fromClass(value))).isExactlyInstanceOf(expected);
    }

    @Test
    void buildGeneric() {
        var sut = new SpecimenFactory(new Context(new Configuration()));

        assertThat(sut.build(new SpecimenType<List<String>>(){})).isExactlyInstanceOf(CollectionSpecimen.class);
        assertThat(sut.build(new SpecimenType<Map<String, Integer>>(){})).isExactlyInstanceOf(MapSpecimen.class);
        assertThat(sut.build(new SpecimenType<Class<String>>(){})).isExactlyInstanceOf(GenericSpecimen.class);
        assertThat(sut.build(new SpecimenType<TestObjectGeneric<String, List<Integer>>>(){})).isExactlyInstanceOf(GenericSpecimen.class);

    }


}
