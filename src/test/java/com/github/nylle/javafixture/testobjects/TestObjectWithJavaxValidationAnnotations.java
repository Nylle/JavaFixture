package com.github.nylle.javafixture.testobjects;


import javax.persistence.Column;
import javax.validation.constraints.Size;

public class TestObjectWithJavaxValidationAnnotations {
    @Size(min = 3, max = 6)
    private String withMinMaxAnnotation;

    @Size(min = 5)
    private String withMinAnnotation;

    @Size(max = 100)
    private String withMaxAnnotation;

    @Column(length = 5)
    private String withColumnLengthAnnotation;

    @Size(max = 100)
    @Column(length = 5)
    private String withColumnLengthAnnotationAndMaxAnnotation;

    public String getWithMinMaxAnnotation() {
        return withMinMaxAnnotation;
    }

    public String getWithMinAnnotation() {
        return withMinAnnotation;
    }

    public String getWithMaxAnnotation() {
        return withMaxAnnotation;
    }

    public String getWithColumnLengthAnnotation() {
        return withColumnLengthAnnotation;
    }

    public String getWithColumnLengthAnnotationAndMaxAnnotation() {
        return withColumnLengthAnnotationAndMaxAnnotation;
    }
}
