package com.neology.parking.controller.model;

import java.math.BigDecimal;

import com.neology.parking.util.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {
    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = Constants.PLATE_REGEX, message = "La placa solo puede contener letras, numeros y guiones")
    @Size(min = 6, max = 15, message = "La placa debe tener entre 6 y 15 caracteres")
    private String licensePlate;
    @NotNull(message = "La fecha de pago es obligatoria")
    @Pattern(regexp = Constants.DATE_ISO_REGEX, message = "La fecha debe ser en formato ISO")
    private String paymentDate;
    @NotNull(message = "El monto de pago es obligatorio")
    @Positive(message = "El monto de pago debe de ser mayor a 0.00")
    private BigDecimal paymentAmount;
}
