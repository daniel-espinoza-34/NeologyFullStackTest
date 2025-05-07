package com.neology.parking.dao;

import com.neology.parking.entity.VehicleType;

public interface VehicleTypeDAO {
    void saveVehicleType(VehicleType type);

    VehicleType findByType(String vehicleType);
}
