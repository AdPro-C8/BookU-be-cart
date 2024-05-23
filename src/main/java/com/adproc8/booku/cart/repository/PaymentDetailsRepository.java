package com.adproc8.booku.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.adproc8.booku.cart.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, UUID> {
    Optional<PaymentDetails> findByCart_UserId(UUID userId);
    @Transactional
    void deleteByCart_UserId(UUID userId);
}
