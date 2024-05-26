package com.adproc8.booku.cart.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    private PaymentDetails paymentDetails;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UUID> bookIds = new HashSet<>();

    @Builder.Default
    private boolean active = true;

    @Transient
    @Builder.Default
    private List<Book> books = List.of();
}
