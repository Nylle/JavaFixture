package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.complex.IContract;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultSpecimenFactoryTest {

    @Test
    void build() {
        var sut = new DefaultSpecimenFactory(new Context(new Configuration()));

        assertThat(sut.build(String.class)).isExactlyInstanceOf(PrimitiveSpecimen.class);
        assertThat(sut.build(Integer.class)).isExactlyInstanceOf(PrimitiveSpecimen.class);
        assertThat(sut.build(int.class)).isExactlyInstanceOf(PrimitiveSpecimen.class);
        assertThat(sut.build(TestEnum.class)).isExactlyInstanceOf(EnumSpecimen.class);
        assertThat(sut.build(TestEnum.class)).isExactlyInstanceOf(EnumSpecimen.class);
        assertThat(sut.build(Map.class)).isExactlyInstanceOf(MapSpecimen.class);
        assertThat(sut.build(List.class)).isExactlyInstanceOf(CollectionSpecimen.class);
        assertThat(sut.build(int[].class)).isExactlyInstanceOf(ArraySpecimen.class);
        assertThat(sut.build(IContract.class)).isExactlyInstanceOf(InterfaceSpecimen.class);
        assertThat(sut.build(Object.class)).isExactlyInstanceOf(ObjectSpecimen.class);
    }

    @Test
    void buildGeneric() throws NoSuchFieldException {
        var sut = new DefaultSpecimenFactory(new Context(new Configuration()));

        Type genericListType = TestObject.class.getDeclaredField("integers").getGenericType();
        Type genericMapType = TestObject.class.getDeclaredField("strings").getGenericType();

        assertThat(sut.build(List.class, genericListType)).isExactlyInstanceOf(CollectionSpecimen.class);
        assertThat(sut.build(Map.class, genericMapType)).isExactlyInstanceOf(MapSpecimen.class);

        assertThatThrownBy(() -> sut.build(TestObject.class, genericListType))
                .isInstanceOf(SpecimenException.class)
                .hasMessageContaining("Unsupported type for generic creation: " + TestObject.class);

    }


}
