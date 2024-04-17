package com.adproc8.booku.cart.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    private final String value;

    private PaymentStatus(String value) {
        this.value = value;
    }
}
