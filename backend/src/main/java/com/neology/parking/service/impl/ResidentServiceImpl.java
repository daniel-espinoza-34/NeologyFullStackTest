package com.neology.parking.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.neology.parking.dao.ResidentDAO;
import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.service.ResidentService;
import com.neology.parking.util.EnumVehicleType;
import com.neology.parking.util.exception.CommonException;
import com.neology.parking.util.exception.CommonNotFoundException;

@Service
public class ResidentServiceImpl implements ResidentService {

    private final VehicleDAO vehicleDAO;
    private final ResidentDAO residentDAO;

    public ResidentServiceImpl(VehicleDAO vehicleDAO, ResidentDAO residentDAO) {
        this.vehicleDAO = vehicleDAO;
        this.residentDAO = residentDAO;
    }

    @Override
    public Resident findByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleDAO.findByLicensePlate(licensePlate);
        if (Objects.isNull(vehicle)) {
            throw new CommonNotFoundException("No existe el vehiculo");
        }
        if (!EnumVehicleType.RESIDENTE.id.equals(vehicle.getVehicleType().getVehicleType())) {
            throw new CommonException("El vehiculo no es de tipo residente");
        }
        Resident resident = residentDAO.findByLicensePlate(licensePlate);
        if (Objects.isNull(resident)) {
            throw new CommonNotFoundException("No existe el residente para el vehiculo");
        }
        return resident;
    }
}
