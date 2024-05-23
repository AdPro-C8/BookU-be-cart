package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.repository.PaymentDetailsRepository;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    public PaymentDetailsServiceImpl(PaymentDetailsRepository paymentDetailsRepository) {
        this.paymentDetailsRepository = paymentDetailsRepository;
    }

    public PaymentDetails save(PaymentDetails paymentDetails) throws IllegalArgumentException {
        if (paymentDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");
            
        return paymentDetailsRepository.save(paymentDetails);
    }

    public Optional<PaymentDetails> findById(UUID id) throws IllegalArgumentException {
        return paymentDetailsRepository.findById(id);
    }

    public Optional<PaymentDetails> findByUserId(UUID userId) throws IllegalArgumentException {
        return paymentDetailsRepository.findByCart_UserId(userId);
    }

    public void deleteById(UUID id) throws IllegalArgumentException {
        paymentDetailsRepository.deleteById(id);
    }

    public void deleteByUserId(UUID userId) throws IllegalArgumentException {
        paymentDetailsRepository.deleteByCart_UserId(userId);
    }
}
