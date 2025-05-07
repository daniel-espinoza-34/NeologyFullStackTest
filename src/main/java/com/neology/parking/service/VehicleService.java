package com.neology.parking.service;

import org.springframework.data.web.PagedModel;

import com.neology.parking.entity.Vehicle;
import com.neology.parking.util.EnumVehicleType;

public interface VehicleService {
    Vehicle addVehicle(String licensePlate, EnumVehicleType vehicleType);

    Vehicle findByLicensePlate(String licensePlate);

    PagedModel<Vehicle> findVehicles(String licensePlate, String sortCol, String sortDir, int page, int resultsPerPage);
}
