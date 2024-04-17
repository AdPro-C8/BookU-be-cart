package com.adproc8.booku.cart.model;

import java.util.List;
import java.util.UUID;

import com.adproc8.booku.cart.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class BookCart {
    
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID cartId;

    @NotNull
    private UUID userId;

    @NotNull
    private int totalPrice;

    @NotNull
    private String paymentStatus;
}
