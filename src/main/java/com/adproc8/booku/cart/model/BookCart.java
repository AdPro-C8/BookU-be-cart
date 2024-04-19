package com.adproc8.booku.cart.model;

import java.util.UUID;

import com.adproc8.booku.cart.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class BookCart {
    
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID cartId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
