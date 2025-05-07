package com.neology.parking.service;

import com.neology.parking.entity.Payment;
import com.neology.parking.service.model.PaymentLog;

public interface PaymentService {
    void addPayment(Payment payment);

    PaymentLog generatePaymentLog(String licensePlate);
}
