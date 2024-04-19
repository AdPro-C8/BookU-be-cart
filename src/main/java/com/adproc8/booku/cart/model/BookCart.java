package com.adproc8.booku.cart.model;

import java.util.List;
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
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToMany
    private List<Book> books;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Override
    public String toString() {
        return String.format(
            "BookCart[id=%s, paymentStatus='%s']",
            id.toString(), paymentStatus.name());
    }

}
