package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.PurchaseDetails;

public interface PurchaseDetailsService {
    PurchaseDetails save(PurchaseDetails purchaseDetails);
    Optional<PurchaseDetails> findById(UUID id);
}
