package com.neology.parking.unit;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.neology.parking.util.Calculation.getTimeDiffMinutes;

import org.junit.jupiter.api.Test;

import com.neology.parking.entity.ParkingRecord;

class TimeCalculationsTest {

    @Test
    public void testTimeCalculation() {
        Instant startTime = Instant.now();
        List<Long> durations = List.of(60L, 30L, 1000L, 24 * 60 * 20L);
        durations.forEach(duration -> {
            assertEquals(
                    duration, getTimeDiffMinutes(
                            new ParkingRecord(0, "TEST-123", startTime, startTime.plus(duration, MINUTES), 0,
                                    BigDecimal.ZERO)),
                    "La diferencia de tiempo no coincide");
        });
    }

}
