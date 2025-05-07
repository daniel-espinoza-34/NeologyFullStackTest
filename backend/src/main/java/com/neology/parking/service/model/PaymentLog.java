package com.neology.parking.service.model;

import java.util.List;

import com.neology.parking.entity.Payment;
import com.neology.parking.entity.Resident;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentLog {
    private final Resident resident;
    private final List<Payment> payments;
}
