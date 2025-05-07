package com.neology.parking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "license_plate", unique = true)
    @NonNull
    private String licensePlate;
    @ManyToOne(targetEntity = VehicleType.class, optional = false)
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    @NonNull
    private VehicleType vehicleType;
    @Column(name = "active_parking")
    private boolean activeParking;
}
