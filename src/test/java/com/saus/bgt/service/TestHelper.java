package com.saus.bgt.service;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestHelper {
    @SneakyThrows
    public static String readFileFromTestResources(String filename) {
        try (InputStream inputStream = TestHelper.class.getClassLoader().getResourceAsStream(filename)) {
            assertNotNull(inputStream);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
