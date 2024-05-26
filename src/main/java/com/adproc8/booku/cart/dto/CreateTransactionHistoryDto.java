package com.adproc8.booku.cart.dto;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CreateTransactionHistoryDto {
    private List<UUID> bookIds;
    private UUID userId;
    private Date purchaseDate;
    private int totalPrice;
}
