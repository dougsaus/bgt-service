package com.saus.bgt.service;

import lombok.Builder;
import lombok.Getter;
import org.apache.mina.util.AvailablePortFinder;

import java.util.Map;

public class MockServerPortHelper {
    private static final Map<String, PortRange> reservations = Map.of(
            "GameMetadataDataFetcherTest", buildRange(10001, 10100),
            "GameDataFetcherTest", buildRange(10101, 10200)
    );

    public static int getAvailablePortForTest(String testName) {
        PortRange range = reservations.get(testName);
        return AvailablePortFinder.getAvailablePorts(range.start, range.end).stream().findFirst().orElseGet(() -> 0);
    }

    @Builder
    @Getter
    public static class PortRange {
        private int start;
        private int end;
    }

    private static PortRange buildRange(int start, int end) {
        return PortRange.builder()
                .start(start)
                .end(end)
                .build();
    }

}