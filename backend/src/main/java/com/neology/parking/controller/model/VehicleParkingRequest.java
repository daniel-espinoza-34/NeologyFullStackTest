package com.neology.parking.controller.model;

import com.neology.parking.util.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VehicleParkingRequest {
    @NotBlank(message = "La placa es requerida")
    @Pattern(regexp = Constants.PLATE_REGEX, message = "La placa solo puede contener letras, numeros y guiones")
    @Size(min = 6, max = 15, message = "La placa debe tener entre 6 y 15 caracteres")
    private String licensePlate;
    @NotNull(message = "La fecha es requerida")
    @Pattern(regexp = Constants.DATE_ISO_REGEX, message = "La fecha debe ser en formato ISO")
    private String dateTime;
}
