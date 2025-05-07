package com.neology.parking.dao;

import java.util.List;

import com.neology.parking.entity.ParkingRecord;

public interface ParkingRecordDAO {
    void saveParkingRecord(ParkingRecord record);

    ParkingRecord getActiveParkingRecord(String licensePlate);

    void closeRecord(ParkingRecord record);

    List<ParkingRecord> findByLicensePlate(String licensePlate);
}
