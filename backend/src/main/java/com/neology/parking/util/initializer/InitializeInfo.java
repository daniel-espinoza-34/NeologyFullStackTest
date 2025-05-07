package com.neology.parking.util.initializer;

import static com.neology.parking.util.EnumVehicleType.NO_RESIDENTE;
import static com.neology.parking.util.EnumVehicleType.OFICIAL;
import static com.neology.parking.util.EnumVehicleType.RESIDENTE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.neology.parking.dao.ParkingRecordDAO;
import com.neology.parking.dao.PaymentDAO;
import com.neology.parking.dao.ResidentDAO;
import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.dao.VehicleTypeDAO;
import com.neology.parking.entity.ParkingRecord;
import com.neology.parking.entity.Payment;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.util.Calculation;

import jakarta.transaction.Transactional;

@Component
public class InitializeInfo implements ApplicationRunner {

    private static final int INITIAL_VEHICLES = 15;
    private static final int MIN_INITIAL_RECORDS = 5;
    private static final int MAX_INITIAL_RECORDS = 25;
    private static final int MAX_PAYMENTS = 5;
    private static final int MAX_MINUTES_STANCE = (60 * 24) - 1; // 1 day - 1 minute

    private final Random random;
    private final PaymentDAO paymentDAO;
    private final VehicleDAO vehicleDAO;
    private final ResidentDAO residentDAO;
    private final VehicleTypeDAO vehicleTypeDAO;
    private final ParkingRecordDAO parkingRecordDAO;

    public InitializeInfo(PaymentDAO paymentDAO, VehicleDAO vehicleDAO, ResidentDAO residentDAO,
            VehicleTypeDAO vehicleTypeDAO,
            ParkingRecordDAO parkingRecordDAO) {
        this.random = new Random();
        this.paymentDAO = paymentDAO;
        this.vehicleDAO = vehicleDAO;
        this.residentDAO = residentDAO;
        this.vehicleTypeDAO = vehicleTypeDAO;
        this.parkingRecordDAO = parkingRecordDAO;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        VehicleType[] types = new VehicleType[] {
                new VehicleType(OFICIAL.id, "Oficial", BigDecimal.ZERO, false, false),
                new VehicleType(RESIDENTE.id, "Residente", new BigDecimal(0.005), false, true),
                new VehicleType(NO_RESIDENTE.id, "No-Residente", new BigDecimal(0.5), true, false),
        };
        for (int i = 0; i < types.length; i++) {
            vehicleTypeDAO.saveVehicleType(types[i]);
        }
        for (int i = 0; i < INITIAL_VEHICLES; i++) {
            long accumulated = 0;
            VehicleType type = types[random.nextInt(types.length)];
            String licensePlate = LicensePlateGen.generateRandomPlate();
            Vehicle vehicle = new Vehicle(licensePlate, type);
            int recordNum = random.nextInt(MIN_INITIAL_RECORDS, MAX_INITIAL_RECORDS);
            for (int j = recordNum; j > 0; j--) {
                Instant startDate = Instant.now().minus(j, ChronoUnit.DAYS);
                Instant endDate = startDate.plus(random.nextInt(MAX_MINUTES_STANCE), ChronoUnit.MINUTES);
                ParkingRecord record = new ParkingRecord(0, licensePlate, startDate, endDate, 0, null);
                long parkingDuration = Calculation.getTimeDiffMinutes(record);
                record.setDuration(parkingDuration);
                if (type.isPayOnExit()) {
                    record.setExitFare(Calculation.calculateRate(type, parkingDuration));
                }
                if (type.isAccumulatedPay()) {
                    accumulated += parkingDuration;
                }
                parkingRecordDAO.saveParkingRecord(record);
            }
            if (random.nextInt(2) == 1) { // Add open record
                parkingRecordDAO.saveParkingRecord(new ParkingRecord(licensePlate, Instant.now()));
                vehicle.setActiveParking(true);
            }
            if (type.isAccumulatedPay()) {
                int paymentsNum = random.nextInt(MAX_PAYMENTS);
                BigDecimal accumulatedRate = Calculation.calculateRate(type, accumulated);
                BigDecimal coveredRate = BigDecimal.ZERO;
                BigDecimal pendingRate = accumulatedRate;

                if (paymentsNum > 0) {
                    BigDecimal paymentAmount = accumulatedRate.divide(
                            BigDecimal.valueOf(paymentsNum), 2, RoundingMode.HALF_EVEN);
                    for (int j = 1; j <= paymentsNum; j++) {
                        paymentDAO.save(new Payment(0,
                                licensePlate, Instant.now().minus(j * 2, ChronoUnit.DAYS), paymentAmount));
                        coveredRate = coveredRate.add(paymentAmount);
                    }
                    pendingRate = accumulatedRate.subtract(coveredRate);
                }

                residentDAO.saveResident(
                        new Resident(licensePlate, accumulated, accumulatedRate, coveredRate, pendingRate));
            }
            vehicleDAO.saveVehicle(vehicle);
        }
    }
}
