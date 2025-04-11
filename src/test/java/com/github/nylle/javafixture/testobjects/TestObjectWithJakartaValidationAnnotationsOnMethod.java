package com.github.nylle.javafixture.testobjects;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;

public class TestObjectWithJakartaValidationAnnotationsOnMethod {

    private String withMinMaxAnnotation;


    private String withMinAnnotation;


    private String withMaxAnnotation;


    private String withColumnLengthAnnotation;


    private String withMaxAnnotationAndColumnLengthAnnotation;

    @Size(min = 3, max = 6)
    public String getWithMinMaxAnnotation() {
        return withMinMaxAnnotation;
    }
    @Size(min = 5)
    public String getWithMinAnnotation() {
        return withMinAnnotation;
    }
    @Size(max = 100)
    public String getWithMaxAnnotation() {
        return withMaxAnnotation;
    }
    @Column(length = 5)
    public String getWithColumnLengthAnnotation() {
        return withColumnLengthAnnotation;
    }
    @Size(max = 100)
    @Column(length = 5)
    public String getWithMaxAnnotationAndColumnLengthAnnotation() {
        return withMaxAnnotationAndColumnLengthAnnotation;
    }
}
