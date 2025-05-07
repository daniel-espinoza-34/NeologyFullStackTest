package com.neology.parking.dao.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import com.neology.parking.dao.VehicleDAO;
import com.neology.parking.entity.Vehicle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class VehicleDAOImpl implements VehicleDAO {

    private EntityManager entityManager;

    public VehicleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {
        entityManager.persist(vehicle);
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) {
        return entityManager.find(Vehicle.class, licensePlate);
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        return entityManager.createQuery("FROM Vehicle", Vehicle.class).getResultList();
    }

    @Override
    public PagedModel<Vehicle> paginatedVehicleList(String licensePlate, Pageable pageable) {
        long totalResults;
        List<Vehicle> vehicles;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        CriteriaQuery<Vehicle> searchQuery = criteriaBuilder.createQuery(Vehicle.class);
        Root<Vehicle> searchRoot = searchQuery.from(Vehicle.class);
        Root<Vehicle> countRoot = countQuery.from(Vehicle.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        if (Objects.nonNull(licensePlate) && !licensePlate.trim().isEmpty()) {
            searchQuery
                    .where(criteriaBuilder.like(searchRoot.get("licensePlate"), String.format("%s%%", licensePlate)));
            countQuery.where(criteriaBuilder.like(countRoot.get("licensePlate"), String.format("%s%%", licensePlate)));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                if (order.isDescending()) {
                    searchQuery.orderBy(criteriaBuilder.desc(searchRoot.get(order.getProperty())));
                } else {
                    searchQuery.orderBy(criteriaBuilder.asc(searchRoot.get(order.getProperty())));
                }
            }
        }
        TypedQuery<Vehicle> query = entityManager.createQuery(searchQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        vehicles = query.getResultList();
        totalResults = entityManager.createQuery(countQuery).getSingleResult();

        return new PagedModel<Vehicle>(new PageImpl<Vehicle>(vehicles, pageable, totalResults));
    }

    @Override
    public void updateParkingStatus(String licensePlate, boolean activeParking) {
        Query updateQuery = entityManager
                .createQuery("UPDATE Vehicle SET activeParking=:parkingStatus WHERE licensePlate=:license");
        updateQuery.setParameter("parkingStatus", activeParking);
        updateQuery.setParameter("license", licensePlate);
        updateQuery.executeUpdate();
    }
}
