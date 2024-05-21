package com.adproc8.booku.cart.dto;

import java.util.List;
import java.util.UUID;

import com.adproc8.booku.cart.model.Book;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class GetCartResponseDto {
    private UUID cartId;
    private UUID userId;
    private UUID checkoutId;
    private List<Book> books;
}
