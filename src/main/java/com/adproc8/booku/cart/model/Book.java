package com.adproc8.booku.cart.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Date publishDate;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int pageCount;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private boolean isAvailable;
}
