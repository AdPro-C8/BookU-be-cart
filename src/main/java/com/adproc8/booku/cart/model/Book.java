package com.adproc8.booku.cart.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
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

    @Override
    public String toString() {
        return String.format(
            "Book[id=%s, title='%s', author='%s', publisher='%s', price='%d', publishDate='%s', isbn='%s', pageCount='%d', photoUrl='%s']",
            id.toString(), title, author, publisher.toString(), price, publishDate.toString(), isbn, pageCount, photoUrl);
    }

}
