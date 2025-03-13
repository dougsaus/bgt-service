package com.saus.bgt.service;

import java.lang.reflect.Method;

public class DisplayNameGenerator extends org.junit.jupiter.api.DisplayNameGenerator.Simple {

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return super.generateDisplayNameForClass(testClass).replace("__", ", ");
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return super.generateDisplayNameForNestedClass(nestedClass).replace("__", ", ");
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return addSpaces(super.generateDisplayNameForMethod(testClass, testMethod).replace("__", ", "));
    }

    private String addSpaces(String name) {
        return name.replace("_", " ");
    }
}