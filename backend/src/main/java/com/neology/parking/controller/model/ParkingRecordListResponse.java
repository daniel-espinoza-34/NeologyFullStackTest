package com.neology.parking.controller.model;

import java.math.BigDecimal;
import java.util.List;

import com.neology.parking.entity.ParkingRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ParkingRecordListResponse {
    @NonNull
    private final List<ParkingRecord> records;
    private final long totalMinutes;
    private BigDecimal totalRate;
}
