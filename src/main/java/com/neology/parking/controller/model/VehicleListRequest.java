package com.neology.parking.controller.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleListRequest {
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "La placa solo puede contener letras, numeros y guiones")
    @Size(min = 6, max = 15, message = "La placa debe tener entre 6 y 15 caracteres")
    private String licensePlateFilter;
    @Pattern(regexp = "licensePlate|vehicleType", message = "No se reconoce el campo para ordenar")
    private String sortBy;
    @Pattern(regexp = "asc|desc", message = "No se reconoce la direccion de ordenamiento")
    private String sortDirection = "desc";
    @PositiveOrZero(message = "La pagina debe ser mayor o igual a 0")
    private int page;
    @Positive(message = "Los resultados por pagina deben ser mayores a 0")
    private int resultsPerPage = 10;
}
