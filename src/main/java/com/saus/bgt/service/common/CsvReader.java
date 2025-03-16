package com.saus.bgt.service.common;

import com.opencsv.CSVReaderHeaderAware;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvReader {
    @SneakyThrows
    public static List<Map<String, String>> readCsvFileAsMap(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return parseCsv(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }

    private static List<Map<String, String>> parseCsv(Reader reader) {
        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader)) {
            List<Map<String, String>> records = new ArrayList<>();
            Map<String, String> record;
            while ((record = csvReader.readMap()) != null) {
                records.add(record);
            }
            return records;
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse CSV file", e);
        }
    }
}