package com.neology.parking.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

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
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "parking_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "license_plate", nullable = false)
    @NonNull
    private String licensePlate;
    @Column(name = "start_time", nullable = false)
    @NonNull
    private Instant startTime;
    @Column(name = "end_time")
    private Instant endTime;
    @Column(name = "duration")
    private long duration;
    @Column(name = "exit_fare")
    private BigDecimal exitFare;
}
