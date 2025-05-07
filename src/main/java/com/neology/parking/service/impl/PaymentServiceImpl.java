package com.neology.parking.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.neology.parking.dao.PaymentDAO;
import com.neology.parking.dao.ResidentDAO;
import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.entity.Payment;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.service.PaymentService;
import com.neology.parking.service.model.PaymentLog;
import com.neology.parking.util.EnumVehicleType;
import com.neology.parking.util.exception.CommonException;
import com.neology.parking.util.exception.CommonNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final VehicleDAO vehicleDAO;
    private final ResidentDAO residentDAO;

    public PaymentServiceImpl(PaymentDAO paymentDAO, VehicleDAO vehicleDAO, ResidentDAO residentDAO) {
        this.paymentDAO = paymentDAO;
        this.vehicleDAO = vehicleDAO;
        this.residentDAO = residentDAO;
    }

    @Override
    @Transactional
    public void addPayment(Payment payment) {
        Vehicle vehicle = vehicleDAO.findByLicensePlate(payment.getLicensePlate());
        if (Objects.isNull(vehicle)) {
            throw new CommonNotFoundException("No existe el vehiculo");
        }
        if (!EnumVehicleType.RESIDENTE.id.equals(vehicle.getVehicleType().getVehicleType())) {
            throw new CommonException("El vehiculo no es de tipo residente");
        }
        Resident resident = residentDAO.findByLicensePlate(payment.getLicensePlate());
        if (Objects.isNull(resident)) {
            throw new CommonNotFoundException("No existe el residente para el vehiculo");
        }
        paymentDAO.save(payment);
        resident.setCoveredAmount(payment.getPaymentAmount().add(resident.getCoveredAmount()));
        resident.setPendingAmount(resident.getAccumulatedRate().subtract(resident.getCoveredAmount()));
        residentDAO.updateResident(resident);
    }

    @Override
    public PaymentLog generatePaymentLog(String licensePlate) {
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

        return new PaymentLog(resident, paymentDAO.findPaymentsByLicensePlate(licensePlate));
    }
}
