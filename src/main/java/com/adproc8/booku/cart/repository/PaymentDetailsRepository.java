package com.adproc8.booku.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.adproc8.booku.cart.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, UUID> {

    @Query("select pd from PaymentDetails pd join Cart c on pd.cart.id = c.id" +
            " where c.userId = ?1" +
            " and pd.paymentStatus = PENDING")
    Optional<PaymentDetails> findByUserId(UUID userId);

    @Transactional
    void deleteByIdAndCart_UserId(UUID id, UUID userId);
}
