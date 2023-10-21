package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.Test;

import static com.github.nylle.javafixture.Configuration.configure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PredefinedSpecimenTest {
    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new PredefinedSpecimen<>(null, new Context( configure())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new PredefinedSpecimen<>( SpecimenType.fromClass( TestEnum.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void createReturnsCachedValue() {
        var type = SpecimenType.fromClass( TestObject.class );
        var testObject = new TestObject( null, null, null );
        var context = new Context( configure() );
        context.overwrite( type,  testObject);
        var sut = new PredefinedSpecimen<>( type, context );

        assertThat( sut.create( null, null ) ).isEqualTo( testObject );
    }

    @Test
    void createReturnsNullIfNoCachedValueIsFound() {
        var type = SpecimenType.fromClass( TestObject.class );
        var context = new Context( configure() );
        var sut = new PredefinedSpecimen<>( type, context );

        assertThat( sut.create( null, null ) ).isNull();
    }
}
