package com.github.nylle.javafixture.specimen;


import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecialSpecimenTest {
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(Configuration.configure());
    }

    @Nested
    @DisplayName("constructor throws exception when called")
    class ConstructorTest {
        @Test
        @DisplayName("with type other than File or URI")
        void onlySpecialType() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(SpecimenType.fromClass(Map.class), context))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + Map.class.getName());
        }

        @Test
        @DisplayName("without type")
        void typeIsRequired() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(null, context))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        @DisplayName("without context")
        void contextIsRequired() {
            assertThatThrownBy(() -> new SpecialSpecimen<>(SpecimenType.fromClass(File.class), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }

    }
    @Test
    @DisplayName("creating two files creates two different files")
    void createFile() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(File.class), context);
        var first = (File)sut.create(noContext(), new Annotation[0]);
        var second = (File)sut.create(noContext(), new Annotation[0]);

        assertThat(first.getAbsolutePath()).isNotEqualTo(second.getAbsolutePath());
    }

    @Test
    @DisplayName("create file w/o context creates file with random name")
    void createFileWithoutContext() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(File.class), context);

        var actual = (File)sut.create(noContext(), new Annotation[0]);

        assertThat( actual ).isNotNull();
        assertThat( actual.getAbsolutePath() ).isNotEmpty();
    }


    @Test
    @DisplayName("create URI creates URI with host, scheme and random path")
    void craeteURI() {
        var sut = new SpecialSpecimen<>(SpecimenType.fromClass(URI.class), context);
        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(URI.class);
        assertThat( ((URI)actual).getScheme() ).isEqualTo("https");
        assertThat( ((URI)actual).getHost() ).isNotNull();
        assertThat( ((URI)actual).getPath() ).isNotEmpty();
    }

}
