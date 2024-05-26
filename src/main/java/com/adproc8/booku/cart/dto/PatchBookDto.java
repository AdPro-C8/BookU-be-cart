package com.adproc8.booku.cart.dto;

import java.sql.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PatchBookDto {
    private UUID id;
    private String publisher;
    private int price;
    private Date publishDate;
    private String isbn;
    private int pageCount;
    private String photoUrl;
    private String category;
    private int downloadCount;
}
