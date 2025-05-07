package com.neology.parking.controller;

import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parking.controller.model.PaymentRequest;
import com.neology.parking.controller.model.VehicleRequest;
import com.neology.parking.entity.Payment;
import com.neology.parking.entity.Resident;
import com.neology.parking.service.PaymentService;
import com.neology.parking.service.ResidentService;
import com.neology.parking.service.model.PaymentLog;

import jakarta.validation.Valid;

@RestController
@RequestMapping("neo/residentes")
public class ResidentController {

    private final PaymentService paymentService;
    private final ResidentService residentService;

    public ResidentController(PaymentService paymentService, ResidentService residentService) {
        this.paymentService = paymentService;
        this.residentService = residentService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/pagos")
    public Payment addPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = new Payment(0,
                request.getLicensePlate(),
                Instant.parse(request.getPaymentDate()),
                request.getPaymentAmount());
        paymentService.addPayment(payment);
        return payment;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/pagos", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentLog generatePaymentLog(@Valid VehicleRequest vehicleRequest) {
        return paymentService.generatePaymentLog(vehicleRequest.getLicensePlate());
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resident getResident(@Valid VehicleRequest vehicleRequest) {
        return residentService.findByLicensePlate(vehicleRequest.getLicensePlate());
    }
}
