package com.neology.parking.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resident_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @NonNull
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
    @NonNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;
    @NonNull
    @Column(name = "payment_amount", nullable = false, scale = 2, precision = 6)
    private BigDecimal paymentAmount;
}
