package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.PaymentDetails;

public interface PaymentDetailsService {
    PaymentDetails save(PaymentDetails paymentDetails);
    Optional<PaymentDetails> findById(UUID id);
    void deleteById(UUID id);
}
