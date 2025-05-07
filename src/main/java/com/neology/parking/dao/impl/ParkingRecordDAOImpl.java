package com.neology.parking.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.neology.parking.dao.ParkingRecordDAO;
import com.neology.parking.entity.ParkingRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class ParkingRecordDAOImpl implements ParkingRecordDAO {

    private EntityManager entityManager;

    public ParkingRecordDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveParkingRecord(ParkingRecord record) {
        entityManager.persist(record);
    }

    @Override
    public ParkingRecord getActiveParkingRecord(String licensePlate) {
        TypedQuery<ParkingRecord> activeRecordQuery = entityManager.createQuery(
                "FROM ParkingRecord WHERE licensePlate=:license AND endTime IS NULL",
                ParkingRecord.class);
        activeRecordQuery.setParameter("license", licensePlate);
        try {
            return activeRecordQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void closeRecord(ParkingRecord record) {
        Query closeQuery = entityManager.createQuery(
                "UPDATE ParkingRecord SET endTime=:closeTime, exitFare=:closeFare WHERE licensePlate=:license AND endTime IS NULL");
        closeQuery.setParameter("closeTime", record.getEndTime());
        closeQuery.setParameter("closeFare", record.getExitFare());
        closeQuery.setParameter("license", record.getLicensePlate());
        closeQuery.executeUpdate();
    }

    @Override
    public List<ParkingRecord> findByLicensePlate(String licensePlate) {
        TypedQuery<ParkingRecord> activeRecordQuery = entityManager.createQuery(
                "FROM ParkingRecord WHERE licensePlate=:license ORDER BY startTime DESC",
                ParkingRecord.class);
        activeRecordQuery.setParameter("license", licensePlate);
        return activeRecordQuery.getResultList();
    }
}
