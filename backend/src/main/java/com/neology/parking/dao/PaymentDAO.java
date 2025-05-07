package com.neology.parking.dao;

import java.util.List;

import com.neology.parking.entity.Payment;

public interface PaymentDAO {
    void save(Payment payment);

    List<Payment> findPaymentsByLicensePlate(String licensePlate);
}
