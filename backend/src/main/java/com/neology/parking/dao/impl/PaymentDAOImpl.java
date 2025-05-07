package com.neology.parking.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.neology.parking.dao.PaymentDAO;
import com.neology.parking.entity.Payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class PaymentDAOImpl implements PaymentDAO {

    private final EntityManager entityManager;

    public PaymentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Payment payment) {
        entityManager.persist(payment);
    }

    @Override
    public List<Payment> findPaymentsByLicensePlate(String licensePlate) {
        TypedQuery<Payment> query = entityManager.createQuery("FROM Payment WHERE licensePlate=:license",
                Payment.class);
        query.setParameter("license", licensePlate);
        return query.getResultList();
    }

}
