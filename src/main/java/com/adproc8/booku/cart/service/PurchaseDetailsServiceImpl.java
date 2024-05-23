package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.PurchaseDetails;
import com.adproc8.booku.cart.repository.PurchaseDetailsRepository;

@Service
public class PurchaseDetailsServiceImpl implements PurchaseDetailsService {

    private final PurchaseDetailsRepository purchaseDetailsRepository;

    @Autowired
    public PurchaseDetailsServiceImpl(PurchaseDetailsRepository purchaseDetailsRepository) {
        this.purchaseDetailsRepository = purchaseDetailsRepository;
    }

    public PurchaseDetails save(PurchaseDetails purchaseDetails) throws IllegalArgumentException {
        if (purchaseDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");
            
        return purchaseDetailsRepository.save(purchaseDetails);
    }

    public Optional<PurchaseDetails> findById(UUID id) throws IllegalArgumentException {
        return purchaseDetailsRepository.findById(id);
    }
}
