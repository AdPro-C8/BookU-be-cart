package com.adproc8.booku.cart.dto;

import com.adproc8.booku.cart.enums.PaymentStatus;

import lombok.Getter;

@Getter
public class UpdatePaymentDetailsRequestDto {
    private String deliveryAddress;
    private PaymentStatus paymentStatus;
}
