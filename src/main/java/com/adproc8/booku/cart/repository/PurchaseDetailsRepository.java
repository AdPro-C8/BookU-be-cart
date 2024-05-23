package com.adproc8.booku.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.cart.model.PurchaseDetails;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, UUID> {
}
