package com.neology.parking.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import com.neology.parking.entity.ParkingRecord;
import com.neology.parking.entity.VehicleType;

public class Calculation {

    public static long getTimeDiffMinutes(ParkingRecord record) {
        return ChronoUnit.MINUTES.between(record.getStartTime(), record.getEndTime());
    }

    public static BigDecimal calculateRate(VehicleType type, long timeDiffMinutes) {
        return type.getRatePerMinute().multiply(new BigDecimal(timeDiffMinutes)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
