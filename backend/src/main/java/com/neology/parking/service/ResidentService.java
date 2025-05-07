package com.neology.parking.service;

import com.neology.parking.entity.Resident;

public interface ResidentService {
    Resident findByLicensePlate(String licensePlate);
}
