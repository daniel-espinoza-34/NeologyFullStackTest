package com.neology.parking.controller;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neology.parking.controller.model.VehicleParkingRequest;
import com.neology.parking.service.VehicleService;
import com.neology.parking.util.EnumVehicleType;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingControllerTest {

    private static final String URL_ENTRADA = "/neo/estancias/entrada";
    private static final String URL_SALIDA = "/neo/estancias/salida";
    private static ObjectMapper mapper;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
    }

    private static String instantToString(Instant instant) {
        return instant.truncatedTo(SECONDS).toString();
    }

    @Test
    public void testNoData() throws Exception {
        // String licensePlate = "TEST-123-NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        // vehicleRequest.setLicensePlate(licensePlate);
        // vehicleRequest.setDateTime(Instant.now().toString());

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La peticion contiene errores"))
                .andExpect(MockMvcResultMatchers.jsonPath("errorDetail").isArray());

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La peticion contiene errores"))
                .andExpect(MockMvcResultMatchers.jsonPath("errorDetail").isArray());
    }

    @Test
    public void testNoLicense() throws Exception {
        // String licensePlate = "TEST-123-NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        // vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa es requerida"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa es requerida"));
    }

    @Test
    public void testLicenseFormat() throws Exception {
        String licensePlate = "TEST123NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa solo puede contener letras, numeros y guiones"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa solo puede contener letras, numeros y guiones"));
    }

    @Test
    public void testLicenseLenght() throws Exception {
        String licensePlate = "TS-T1";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa debe tener entre 6 y 15 caracteres"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La placa debe tener entre 6 y 15 caracteres"));
    }

    @Test
    public void testNoDate() throws Exception {
        String licensePlate = "TEST-123-NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        // vehicleRequest.setDateTime(Instant.now().toString().substring(5));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La fecha es requerida"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La fecha es requerida"));
    }

    @Test
    public void testDateFormat() throws Exception {
        String licensePlate = "TEST-123-NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()).substring(5));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La fecha debe ser en formato ISO"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La fecha debe ser en formato ISO"));
    }

    @Test
    public void testNotExists() throws Exception {
        String licensePlate = "TEST-123-NE";
        // vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("El vehiculo no se encuentra registrado"));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("El vehiculo no se encuentra registrado"));
    }

    @Test
    @Rollback
    public void testCloseNotOpen() throws Exception {
        String licensePlate = "TEST-123-CNO";
        vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(instantToString(Instant.now()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("El vehiculo no se encuentra en la estancia"));
    }

    @Test
    @Rollback
    public void testOpenNotClosed() throws Exception {
        String licensePlate = "TEST-123-ONC";
        String startTimeStr = instantToString(Instant.now());
        String endTimeStr = instantToString(Instant.now().plus(30, MINUTES));
        vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(startTimeStr);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());

        vehicleRequest.setDateTime(endTimeStr);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("El vehiculo ya se encuentra en la estancia"));
    }

    @Test
    @Rollback
    public void testCloseBeforeOpen() throws Exception {
        String licensePlate = "TEST-123-CBO";
        String startTimeStr = instantToString(Instant.now());
        String endTimeStr = instantToString(Instant.now().minus(30, MINUTES));
        vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(startTimeStr);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());

        vehicleRequest.setDateTime(endTimeStr);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage")
                        .value("La fecha de salida no puede ser anterior a la fecha de entrada"));
    }

    @Test
    @Rollback
    public void testCloseOpenOficial() throws Exception {
        String licensePlate = "TEST-123-O";
        String startTimeStr = instantToString(Instant.now());
        String endTimeStr = instantToString(Instant.now().plus(30, MINUTES));
        vehicleService.addVehicle(licensePlate, EnumVehicleType.OFICIAL);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(startTimeStr);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());

        vehicleRequest.setDateTime(endTimeStr);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").value(endTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());
    }

    @Test
    @Rollback
    public void testCloseOpenResident() throws Exception {
        String licensePlate = "TEST-123-R";
        String startTimeStr = instantToString(Instant.now());
        String endTimeStr = instantToString(Instant.now().plus(30, MINUTES));
        vehicleService.addVehicle(licensePlate, EnumVehicleType.RESIDENTE);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(startTimeStr);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());

        vehicleRequest.setDateTime(endTimeStr);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").value(endTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());
    }

    @Test
    @Rollback
    public void testCloseOpenNoResident() throws Exception {
        String licensePlate = "TEST-123-N";
        String startTimeStr = instantToString(Instant.now());
        String endTimeStr = instantToString(Instant.now().plus(30, MINUTES));
        vehicleService.addVehicle(licensePlate, EnumVehicleType.NO_RESIDENTE);
        VehicleParkingRequest vehicleRequest = new VehicleParkingRequest();
        vehicleRequest.setLicensePlate(licensePlate);
        vehicleRequest.setDateTime(startTimeStr);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_ENTRADA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").doesNotExist());

        vehicleRequest.setDateTime(endTimeStr);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_SALIDA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("licensePlate").value(licensePlate))
                .andExpect(MockMvcResultMatchers.jsonPath("startTime").value(startTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("endTime").value(endTimeStr))
                .andExpect(MockMvcResultMatchers.jsonPath("duration").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("exitFare").value(15));
    }
}
