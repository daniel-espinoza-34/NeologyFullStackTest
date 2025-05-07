package com.neology.parking.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.neology.parking.entity.Vehicle;

public interface VehicleDAO {
    void saveVehicle(Vehicle vehicle);

    Vehicle findByLicensePlate(String licensePlate);

    List<Vehicle> findAllVehicles();

    PagedModel<Vehicle> paginatedVehicleList(String licensePlate, Pageable pageable);

    void updateParkingStatus(String licensePlate, boolean activeParking);
}
