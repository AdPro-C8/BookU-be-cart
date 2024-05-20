package com.adproc8.booku.cart.model;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Book {
    private UUID id;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private Date publishDate;
    private String isbn;
    private int pageCount;
    private String photoUrl;
    private String category;
    private int downloadCount;
}
