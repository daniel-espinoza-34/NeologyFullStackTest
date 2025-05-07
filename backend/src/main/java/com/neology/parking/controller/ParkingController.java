package com.neology.parking.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parking.controller.model.ParkingRecordListResponse;
import com.neology.parking.controller.model.VehicleParkingRequest;
import com.neology.parking.controller.model.VehicleRequest;
import com.neology.parking.entity.ParkingRecord;
import com.neology.parking.service.VehicleParkingService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("neo/estancias")
public class ParkingController {

    private final VehicleParkingService vehicleParkingService;

    public ParkingController(VehicleParkingService vehicleParkingService) {
        this.vehicleParkingService = vehicleParkingService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/entrada", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ParkingRecord vehilceEnter(@Valid @RequestBody VehicleParkingRequest vehicleInfo) {
        return vehicleParkingService.enterParking(vehicleInfo.getLicensePlate(), vehicleInfo.getDateTime());
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/salida", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ParkingRecord vehicleExit(@Valid @RequestBody VehicleParkingRequest vehicleInfo) {
        return vehicleParkingService.exitParking(vehicleInfo.getLicensePlate(), vehicleInfo.getDateTime());
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParkingRecordListResponse findVehicleParkingRecords(@Valid VehicleRequest vehicleRequest) {
        List<ParkingRecord> records = vehicleParkingService
                .findParkingRecordsByLicensePlate(vehicleRequest.getLicensePlate());
        long totalMinutes = records.stream().mapToLong(rec -> rec.getDuration()).sum();
        BigDecimal totalRate = records.stream()
                .map(rec -> rec.getExitFare())
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add).orElse(null);
        return new ParkingRecordListResponse(records, totalMinutes, totalRate);
    }
}
