package com.neology.parking.dao.impl;

import org.springframework.stereotype.Repository;

import com.neology.parking.dao.VehicleTypeDAO;
import com.neology.parking.entity.VehicleType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class VehicleTypeDAOImpl implements VehicleTypeDAO {

    private EntityManager entityManager;

    public VehicleTypeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveVehicleType(VehicleType type) {
        entityManager.persist(type);
    }

    @Override
    public VehicleType findByType(String vehicleType) {
        TypedQuery<VehicleType> activeRecordQuery = entityManager.createQuery(
                "FROM VehicleType WHERE vehicleType=:type",
                VehicleType.class);
        activeRecordQuery.setParameter("type", vehicleType);
        return activeRecordQuery.getSingleResult();
    }
}
