package com.neology.parking.service.impl;

import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.dao.VehicleTypeDAO;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.service.VehicleService;
import com.neology.parking.util.EnumVehicleType;
import com.neology.parking.util.exception.CommonException;
import com.neology.parking.util.exception.CommonNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private VehicleDAO vehicleDAO;
    private VehicleTypeDAO vehicleTypeDAO;

    public VehicleServiceImpl(VehicleDAO vehicleDAO, VehicleTypeDAO vehicleTypeDAO) {
        this.vehicleDAO = vehicleDAO;
        this.vehicleTypeDAO = vehicleTypeDAO;
    }

    @Override
    @Transactional
    public Vehicle addVehicle(String licensePlate, EnumVehicleType vehicleType) {
        return addVehicle(licensePlate, vehicleType.id);
    }

    private Vehicle addVehicle(String licensePlate, String vehicleType) {
        if (Objects.nonNull(vehicleDAO.findByLicensePlate(licensePlate))) {
            throw new CommonException("La placa ya se encuentra registrada");
        }
        // check no duplicate license
        VehicleType type = vehicleTypeDAO.findByType(vehicleType);
        if (Objects.isNull(type)) {
            throw new CommonNotFoundException("El tipo de vehiculo no existe");
        }
        Vehicle newVehicle = new Vehicle(licensePlate, type);
        // add Vehicle with type oficial
        vehicleDAO.saveVehicle(newVehicle);
        return newVehicle;
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleDAO.findByLicensePlate(licensePlate);
        if (Objects.isNull(vehicle)) {
            throw new CommonNotFoundException("La placa de vehiculo no se encuentra registrada");
        }
        return vehicle;
    }

    @Override
    public PagedModel<Vehicle> findVehicles(String licensePlate, String sortBy, String sortDir, int page,
            int resultsPerPage) {
        Sort sorting;
        if (Objects.isNull(sortBy) || sortBy.trim().isEmpty()) {
            sorting = Sort.unsorted();
        } else {
            sorting = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        }
        return vehicleDAO.paginatedVehicleList(licensePlate, PageRequest.of(page, resultsPerPage, sorting));
    }
}
