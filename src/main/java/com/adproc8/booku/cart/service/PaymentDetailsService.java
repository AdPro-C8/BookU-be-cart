package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.PaymentDetails;

public interface PaymentDetailsService {
    PaymentDetails save(PaymentDetails paymentDetails);
    PaymentDetails save(PaymentDetails paymentDetails, String authHeader);
    Optional<PaymentDetails> findById(UUID id);
    Optional<PaymentDetails> findByUserId(UUID userId);
    void deleteById(UUID id);
    void deleteByIdAndUserId(UUID id, UUID userId);
}
