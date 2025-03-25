package com.saus.bgt.service.common;

import com.saus.bgt.service.GameTrackerNameGeneratingTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@GameTrackerNameGeneratingTest
class CsvReaderTest {

    @Test
    void given_valid_csv__when_readCsvFileAsMap__then_should_return_map_with_all_columns() {
        String filePath = Paths.get("src/test/resources/scenarios/util/csv-read-all/games.csv").toString();

        List<Map<String, String>> records = CsvReader.readCsvFileAsMap(filePath);

        assertThat(records).hasSize(3);

        assertThat(records.get(0))
                .containsEntry("id", "1")
                .containsEntry("name", "Too Many Bones")
                .containsEntry("type", "coop");

        assertThat(records.get(1))
                .containsEntry("id", "2")
                .containsEntry("name", "Nemesis")
                .containsEntry("type", "coop");

        assertThat(records.get(2))
                .containsEntry("id", "3")
                .containsEntry("name", "Terraforming Mars")
                .containsEntry("type", "pvp");
    }

    @Test
    void given_csv_doesnt_exist__when_readCsvFileAsMap__then_exception_thrown() {
        String filePath = Paths.get("src/test/resources/scenarios/util/csv-read-all/some-non-existant-file.csv").toString();
        Exception e = assertThrows(RuntimeException.class, () -> CsvReader.readCsvFileAsMap(filePath));
        assertThat(e).hasMessage("Failed to read CSV file: src/test/resources/scenarios/util/csv-read-all/some-non-existant-file.csv");
        assertThat(e).cause().isInstanceOf(IOException.class);
    }

    @Test
    void given_invalid_csv__when_readCsvFileAsMap__then_exception_thrown() {
        String filePath = Paths.get("src/test/resources/scenarios/util/invalid/games-missing-header.csv").toString();
        Exception e = assertThrows(RuntimeException.class, () -> CsvReader.readCsvFileAsMap(filePath));
        assertThat(e).hasMessage("Unable to parse CSV file");
        assertThat(e).cause().isInstanceOf(IOException.class);
    }

}