package com.neology.parking.controller;

import static com.neology.parking.util.EnumVehicleType.NO_RESIDENTE;
import static com.neology.parking.util.EnumVehicleType.OFICIAL;
import static com.neology.parking.util.EnumVehicleType.RESIDENTE;

import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parking.controller.model.VehicleListRequest;
import com.neology.parking.controller.model.VehicleRequest;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("neo/vehiculos")
public class VehicleController {

    private final VehicleService addVehicleService;

    public VehicleController(VehicleService addVehicleService) {
        this.addVehicleService = addVehicleService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/oficiales", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle addOficialVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return addVehicleService.addVehicle(vehicleRequest.getLicensePlate(), OFICIAL);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/residentes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle addResidentVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return addVehicleService.addVehicle(vehicleRequest.getLicensePlate(), RESIDENTE);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/no-residentes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle addGeneralVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return addVehicleService.addVehicle(vehicleRequest.getLicensePlate(), NO_RESIDENTE);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<Vehicle> findVehicles(@Valid VehicleListRequest requestInfo) {
        return addVehicleService.findVehicles(requestInfo.getLicensePlateFilter(), requestInfo.getSortBy(),
                requestInfo.getSortDirection(), requestInfo.getPage(), requestInfo.getResultsPerPage());
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/{licensePlate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle findVehicleByLicense(@Valid VehicleRequest requestInfo) {
        return addVehicleService.findByLicensePlate(requestInfo.getLicensePlate());
    }

}
