package com.neology.parking.dao.impl;

import org.springframework.stereotype.Repository;

import com.neology.parking.dao.ResidentDAO;
import com.neology.parking.entity.Resident;

import jakarta.persistence.EntityManager;

@Repository
public class ResidentDAOImpl implements ResidentDAO {

    private EntityManager entityManager;

    public ResidentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveResident(Resident resident) {
        entityManager.persist(resident);
    }

    @Override
    public Resident findByLicensePlate(String licensePlate) {
        return entityManager.find(Resident.class, licensePlate);
    }

    @Override
    public void updateResident(Resident resident) {
        entityManager.merge(resident);
    }
}
