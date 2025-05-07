package com.neology.parking.service;

import java.util.List;

import com.neology.parking.entity.ParkingRecord;

public interface VehicleParkingService {
    ParkingRecord enterParking(String licensePlate, String startTime);

    ParkingRecord exitParking(String licensePlate, String endTime);

    List<ParkingRecord> findParkingRecordsByLicensePlate(String licensePlate);
}
