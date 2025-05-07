package com.neology.parking.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neology.parking.dao.ParkingRecordDAO;
import com.neology.parking.dao.ResidentDAO;
import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.entity.ParkingRecord;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.service.VehicleParkingService;
import com.neology.parking.util.exception.CommonException;
import com.neology.parking.util.exception.CommonNotFoundException;

import static com.neology.parking.util.Calculation.getTimeDiffMinutes;
import static com.neology.parking.util.Calculation.calculateRate;

@Service
public class VehicleParkingServiceImpl implements VehicleParkingService {

    private VehicleDAO vehicleDAO;
    private ResidentDAO residentDAO;
    private ParkingRecordDAO parkingRecordDAO;

    public VehicleParkingServiceImpl(VehicleDAO vehicleDAO, ResidentDAO residentDAO,
            ParkingRecordDAO parkingRecordDAO) {
        this.vehicleDAO = vehicleDAO;
        this.residentDAO = residentDAO;
        this.parkingRecordDAO = parkingRecordDAO;
    }

    @Override
    @Transactional
    public ParkingRecord enterParking(String licensePlate, String startTime) {
        if (Objects.isNull(vehicleDAO.findByLicensePlate(licensePlate))) {
            throw new CommonNotFoundException("El vehiculo no se encuentra registrado");
        }
        if (Objects.nonNull(parkingRecordDAO.getActiveParkingRecord(licensePlate))) {
            throw new CommonException("El vehiculo ya se encuentra en la estancia");
        }
        ParkingRecord record = new ParkingRecord(licensePlate, Instant.parse(startTime));
        parkingRecordDAO.saveParkingRecord(record);
        vehicleDAO.updateParkingStatus(licensePlate, true);
        return record;
    }

    @Override
    @Transactional
    public ParkingRecord exitParking(String licensePlate, String endTime) {
        Vehicle vehicle = vehicleDAO.findByLicensePlate(licensePlate);
        if (Objects.isNull(vehicle)) {
            throw new CommonNotFoundException("El vehiculo no se encuentra registrado");
        }
        ParkingRecord record = parkingRecordDAO.getActiveParkingRecord(licensePlate);
        if (Objects.isNull(record)) {
            throw new CommonNotFoundException("El vehiculo no se encuentra en la estancia");
        }
        record.setEndTime(Instant.parse(endTime));
        if (record.getStartTime().isAfter(record.getEndTime())) {
            throw new CommonException("La fecha de salida no puede ser anterior a la fecha de entrada");
        }
        record.setDuration(getTimeDiffMinutes(record));
        if (vehicle.getVehicleType().isAccumulatedPay()) {
            Resident resident = Optional.<Resident>ofNullable(residentDAO.findByLicensePlate(licensePlate))
                    .orElseGet(() -> new Resident(licensePlate, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            resident.setAccumulatedTime(resident.getAccumulatedTime() + record.getDuration());
            resident.setAccumulatedRate(calculateRate(vehicle.getVehicleType(), resident.getAccumulatedTime()));
            resident.setPendingAmount(resident.getAccumulatedRate().subtract(resident.getCoveredAmount()));
            residentDAO.updateResident(resident);
        }
        if (vehicle.getVehicleType().isPayOnExit()) {
            record.setExitFare(calculateRate(vehicle.getVehicleType(), record.getDuration()));
        }
        parkingRecordDAO.closeRecord(record);
        vehicleDAO.updateParkingStatus(licensePlate, false);
        return record;
    }

    @Override
    public List<ParkingRecord> findParkingRecordsByLicensePlate(String licensePlate) {
        if (Objects.isNull(vehicleDAO.findByLicensePlate(licensePlate))) {
            throw new CommonNotFoundException("El vehiculo no se encuentra registrado");
        }
        return parkingRecordDAO.findByLicensePlate(licensePlate);
    }
}
