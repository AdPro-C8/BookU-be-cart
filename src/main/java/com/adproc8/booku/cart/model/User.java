package com.adproc8.booku.cart.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String role;
}
