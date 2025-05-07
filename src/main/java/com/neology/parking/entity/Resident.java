package com.neology.parking.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "resident")
public class Resident {
    @Id
    @NonNull
    @Column(name = "license_plate", unique = true)
    private String licencePlate;
    @Column(name = "accumulated_time")
    private long accumulatedTime;
    @NonNull
    @Column(name = "accumulated_rate")
    private BigDecimal accumulatedRate;
    @Column(name = "covered_rate")
    private BigDecimal coveredAmount;
    @Column(name = "pending_rate")
    private BigDecimal pendingAmount;
}
