package com.neology.parking.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neology.parking.controller.model.VehicleRequest;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    private static ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void noInvalidLicnesePlate() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setLicensePlate("TEST123");

        mockMvc.perform(MockMvcRequestBuilders.post("/neo/vehiculos/residentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa solo puede contener letras, numeros y al menos un guion"));
    }

    @Test
    public void addOficialVehicle() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setLicensePlate("TEST-123-AO");

        mockMvc.perform(MockMvcRequestBuilders.post("/neo/vehiculos/oficiales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addResidentVehicle() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setLicensePlate("TEST-123-AR");

        mockMvc.perform(MockMvcRequestBuilders.post("/neo/vehiculos/residentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void addGeneralVehicle() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setLicensePlate("TEST-123-AN");

        mockMvc.perform(MockMvcRequestBuilders.post("/neo/vehiculos/no-residentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void noRepeatedVehicle() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setLicensePlate("TEST-123-AO");

        mockMvc.perform(MockMvcRequestBuilders.post("/neo/vehiculos/residentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage").value("La placa ya se encuentra registrada"));
    }

}
