package com.github.nylle.javafixture.testobjects;

import jakarta.validation.constraints.Size;

public class TestObjectWithJakartaValidationAnnotations {
    @Size(min = 3, max = 6)
    private String withMinMaxAnnotation;

    @Size(min = 5)
    private String withMinAnnotation;

    @Size(max = 100)
    private String withMaxAnnotation;

    public String getWithMinMaxAnnotation() {
        return withMinMaxAnnotation;
    }

    public String getWithMinAnnotation() {
        return withMinAnnotation;
    }

    public String getWithMaxAnnotation() {
        return withMaxAnnotation;
    }
}
