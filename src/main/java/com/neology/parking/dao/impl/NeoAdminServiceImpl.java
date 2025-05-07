package com.neology.parking.dao.impl;

import org.springframework.stereotype.Service;

import com.neology.parking.dao.NeoAdminService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class NeoAdminServiceImpl implements NeoAdminService {

    private final EntityManager entityManager;

    public NeoAdminServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void restartTracking() {
        entityManager.createQuery("DELETE FROM Payment").executeUpdate();
        entityManager.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        entityManager.createQuery("UPDATE Vehicle SET activeParking=false").executeUpdate();
        entityManager.createQuery(
                "UPDATE Resident SET accumulatedTime=0, accumulatedRate=0, coveredAmount=0, pendingAmount=0")
                .executeUpdate();
    }
}
