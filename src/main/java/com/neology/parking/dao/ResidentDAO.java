package com.neology.parking.dao;

import com.neology.parking.entity.Resident;

public interface ResidentDAO {
    void saveResident(Resident resident);

    Resident findByLicensePlate(String licensePlate);

    void updateResident(Resident resident);
}
