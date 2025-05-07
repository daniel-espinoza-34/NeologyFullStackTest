package com.neology.parking.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle_type")
public class VehicleType {
    @Id
    @Column(name = "vehicle_type")
    private String vehicleType;
    @Column(name = "name")
    private String name;
    @JsonIgnore
    @Column(name = "rate_per_minute", scale = 3, precision = 4)
    private BigDecimal ratePerMinute;
    @JsonIgnore
    @Column(name = "pay_on_exit")
    private boolean payOnExit;
    @JsonIgnore
    @Column(name = "accumulated_pay")
    private boolean accumulatedPay;
}
