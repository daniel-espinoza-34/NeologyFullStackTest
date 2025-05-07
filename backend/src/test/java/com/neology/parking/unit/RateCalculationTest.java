package com.neology.parking.unit;

import org.junit.jupiter.api.Test;

import com.neology.parking.entity.VehicleType;

import static com.neology.parking.util.Calculation.calculateRate;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

class RateCalculationTest {

    @Test
    public void rateCalculationTest() {
        BigDecimal sameVal = BigDecimal.valueOf(1);
        BigDecimal resVal = BigDecimal.valueOf(0.05);
        BigDecimal noResVal = BigDecimal.valueOf(0.5);
        VehicleType testType = new VehicleType("test-123", "Test-123", sameVal, false, false);
        VehicleType residentType = new VehicleType("test-resident", "Test-Resident", resVal, false, false);
        VehicleType noResidentType = new VehicleType("test-no-resident", "Test-No-Resident", noResVal, false, false);

        List<Long> durations = List.of(1L, 10L, 100L, 60L, 7L, 3L, 5L);
        durations.forEach(duration -> {
            BigDecimal expectedAmountTest = BigDecimal.valueOf(duration).setScale(2);
            assertEquals(expectedAmountTest, calculateRate(testType, duration));
            BigDecimal expectedAmountRes = resVal.multiply(BigDecimal.valueOf(duration)).setScale(2, HALF_EVEN);
            assertEquals(expectedAmountRes, calculateRate(residentType, duration));
            BigDecimal expectedAmountNoRes = noResVal.multiply(BigDecimal.valueOf(duration)).setScale(2, HALF_EVEN);
            assertEquals(expectedAmountNoRes, calculateRate(noResidentType, duration));
        });
    }

}
